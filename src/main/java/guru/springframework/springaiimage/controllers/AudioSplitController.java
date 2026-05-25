package guru.springframework.springaiimage.controllers;

import guru.springframework.springaiimage.services.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class AudioSplitController {

    private final OpenAIService openAIService;

    @PostMapping(
            value = "/audio/split",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "application/zip"
    )
    public ResponseEntity<byte[]> splitAudio(@RequestPart("file") MultipartFile file) {
        byte[] zip = openAIService.splitAudio(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audio-parts.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(zip);
    }
}

