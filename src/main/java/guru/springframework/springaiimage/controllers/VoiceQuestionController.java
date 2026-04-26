package guru.springframework.springaiimage.controllers;

import guru.springframework.springaiimage.model.Question;
import guru.springframework.springaiimage.services.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by jt, Spring Framework Guru.
 */
@RequiredArgsConstructor
@RestController
public class VoiceQuestionController {

    private final OpenAIService openAIService;


    @PostMapping(value = "/talk", produces = "audio/mpeg")
    public byte[] getAudio(@RequestBody Question question) {
        return openAIService.getSpeech(question);
    }
}
