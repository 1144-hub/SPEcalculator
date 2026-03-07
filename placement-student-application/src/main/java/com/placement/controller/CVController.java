package com.placement.controller;

import com.placement.domain.CVMetadata;
import com.placement.service.CVStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students/{studentId}/cv")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CVController {

    private final CVStorageService cvStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCV(@PathVariable Long studentId,
                                      @RequestParam("file") MultipartFile file) {
        try {
            CVMetadata metadata = cvStorageService.uploadCV(studentId, file);
            return ResponseEntity.status(201).body(Map.of(
                    "id", metadata.getId(),
                    "originalFileName", metadata.getOriginalFileName(),
                    "storedFileName", metadata.getStoredFileName(),
                    "fileSizeBytes", metadata.getFileSizeBytes() != null ? metadata.getFileSizeBytes() : 0,
                    "uploadedAt", metadata.getUploadedAt().toString()
            ));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload file: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Map<String, Object>> listCVs(@PathVariable Long studentId) {
        return cvStorageService.getCVsByStudentId(studentId).stream()
                .map(m -> Map.<String, Object>of(
                        "id", m.getId(),
                        "originalFileName", m.getOriginalFileName(),
                        "storedFileName", m.getStoredFileName(),
                        "fileSizeBytes", m.getFileSizeBytes() != null ? m.getFileSizeBytes() : 0,
                        "uploadedAt", m.getUploadedAt().toString()
                ))
                .toList();
    }

    @GetMapping("/{cvId}/download")
    public ResponseEntity<byte[]> downloadCV(@PathVariable Long studentId,
                                             @PathVariable Long cvId) {
        try {
            CVMetadata metadata = cvStorageService.getCVById(cvId);
            if (!metadata.getStudent().getId().equals(studentId)) {
                return ResponseEntity.notFound().build();
            }
            byte[] content = cvStorageService.getCVFile(cvId);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getOriginalFileName() + "\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
