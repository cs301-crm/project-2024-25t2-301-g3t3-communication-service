package com.cs301.communication_service.controllers;

import com.cs301.communication_service.services.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/communications/getPresignedURL")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }
    
    // Use GET, and expect the fileName as a query parameter, e.g.: /getPresignedURL?fileName=upload.pdf
    @GetMapping
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam(name = "fileName", required = false) String fileName) {
        String url = s3Service.getPresignedUrl(fileName);
        if (url != null) {
            return ResponseEntity.ok(Collections.singletonMap("url", url));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to generate presigned URL from controller"));
        }
    }
}
