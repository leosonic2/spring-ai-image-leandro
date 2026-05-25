package guru.springframework.springaiimage.services;

import guru.springframework.springaiimage.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.ai.openai.api.OpenAiAudioApi.StructuredResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ImageModel imageModel;

    private final ChatModel  chatModel;

    private final OpenAiAudioSpeechModel speechModel;

    private final OpenAiAudioApi openAiAudioApi;

    @Override
    public String getDescription(MultipartFile file) {

        OpenAiChatOptions chatOptions =OpenAiChatOptions.builder()
                .model("gpt-5.4")
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text("return the description of the image in a json format")
                .media(new Media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
                .build();

        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),chatOptions));


        return response.getResult().getOutput().getText();
    }

    @Override
    public byte[] getSpeech(Question question) {
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .speed(1.0f)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .build();
        String textInPortuguese = "[Fale em português do Brasil] " + question.question();
        SpeechPrompt speechPrompt = new SpeechPrompt(textInPortuguese, options);

        SpeechResponse speechResponse = speechModel.call(speechPrompt);

        return speechResponse.getResult().getOutput();
    }

    @Override
    public byte[] getImage(Question question) {

        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .height(1024)
                .width(1024)
                .model("gpt-image-1.5")
                .quality("high")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(question.question(),options);

        ImageResponse imageCall = imageModel.call(imagePrompt);

        return Base64.getDecoder().decode(imageCall.getResult().getOutput().getB64Json());
    }

    @Override
    public byte[] splitAudio(MultipartFile file) {
        try {
            byte[] audioBytes = file.getBytes();
            if (audioBytes.length == 0) {
                throw new IllegalArgumentException("Empty audio file");
            }

            // 1) Use OpenAI Whisper to transcribe with segment timestamps (verbose_json).
            OpenAiAudioApi.TranscriptionRequest request = OpenAiAudioApi.TranscriptionRequest.builder()
                    .file(audioBytes)
                    .model(OpenAiAudioApi.WhisperModel.WHISPER_1.value)
                    .responseFormat(TranscriptResponseFormat.VERBOSE_JSON)
                    .temperature(0f)
                    .build();

            StructuredResponse structured = openAiAudioApi
                    .createTranscription(request, StructuredResponse.class)
                    .getBody();

            if (structured == null || structured.duration() == null) {
                throw new IllegalStateException("The audio duration could not be obtained.");
            }

            float totalDuration = structured.duration();
            if (totalDuration <= 0f) {
                throw new IllegalStateException("Duração inválida do áudio");
            }

            // 2) Determine 3 cut-off points (in seconds) by choosing the end of the segment.
            //    closest to each quarter of the total duration — natural points identified by OpenAI.
            List<Float> cutTimes = new ArrayList<>();
            for (int k = 1; k <= 3; k++) {
                float target = (totalDuration * k) / 4f;
                float chosen = target;
                float bestDiff = Float.MAX_VALUE;
                if (structured.segments() != null) {
                    for (var segment : structured.segments()) {
                        float diff = Math.abs(segment.end() - target);
                        if (diff < bestDiff) {
                            bestDiff = diff;
                            chosen = segment.end();
                        }
                    }
                }
                cutTimes.add(chosen);
            }
            cutTimes.add(totalDuration);

            // 3) Converts times into byte offsets proportionally to the total duration.
            //    (MP3 approximation — works well for files with a constant bitrate).
            int totalBytes = audioBytes.length;
            List<Integer> byteOffsets = new ArrayList<>();
            byteOffsets.add(0);
            for (Float t : cutTimes) {
                int offset = Math.round((t / totalDuration) * totalBytes);
                offset = Math.max(0, Math.min(totalBytes, offset));
                byteOffsets.add(offset);
            }

            // 4) Pack all 4 parts in a ZIP bag.
            ByteArrayOutputStream zipBuffer = new ByteArrayOutputStream();
            try (ZipOutputStream zip = new ZipOutputStream(zipBuffer)) {
                for (int i = 0; i < 4; i++) {
                    int start = byteOffsets.get(i);
                    int end = byteOffsets.get(i + 1);
                    if (end <= start) {
                        continue;
                    }
                    byte[] part = new byte[end - start];
                    System.arraycopy(audioBytes, start, part, 0, end - start);

                    ZipEntry entry = new ZipEntry(String.format("part-%d.mp3", i + 1));
                    zip.putNextEntry(entry);
                    zip.write(part);
                    zip.closeEntry();
                }
            }

            return zipBuffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Audio splitting failed.", e);
        }
    }

}


