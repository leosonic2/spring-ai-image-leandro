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
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ImageModel imageModel;

    private final ChatModel  chatModel;

    private final OpenAiAudioSpeechModel speechModel;

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

}


