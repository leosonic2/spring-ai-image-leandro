package guru.springframework.springaiimage.services;

import guru.springframework.springaiimage.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ImageModel imageModel;


    @Override
    public byte[] getImage(Question question) {

        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .withHeight(1024)
                .width(1024)
                .withModel("gpt-image-1.5")
                .withQuality("high")
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(question.question(),options);

        ImageResponse imageCall = imageModel.call(imagePrompt);

        return Base64.getDecoder().decode(imageCall.getResult().getOutput().getB64Json());
    }
}


