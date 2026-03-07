package com.placement.service;

import com.placement.config.FileUploadConfig;
import com.placement.domain.CVMetadata;
import com.placement.domain.Student;
import com.placement.repository.CVMetadataRepository;
import com.placement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for CV upload and metadata persistence.
 * Links student academic profiles with CV files.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CVStorageService {

    private final FileUploadConfig fileUploadConfig;
    private final CVMetadataRepository cvMetadataRepository;
    private final StudentRepository studentRepository;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    @Transactional
    public CVMetadata uploadCV(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            originalFilename = "cv.pdf";
        }

        String storedFileName = UUID.randomUUID().toString() + "_" + sanitizeFileName(originalFilename);
        Path uploadPath = Paths.get(fileUploadConfig.getUploadDir());
        Path filePath = uploadPath.resolve(storedFileName);

        Files.write(filePath, file.getBytes());
        log.info("CV saved to: {}", filePath);

        CVMetadata metadata = CVMetadata.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFileName)
                .filePath(filePath.toAbsolutePath().toString())
                .fileSizeBytes(file.getSize())
                .contentType(file.getContentType())
                .uploadedAt(LocalDateTime.now())
                .student(student)
                .build();

        return cvMetadataRepository.save(metadata);
    }

    public List<CVMetadata> getCVsByStudentId(Long studentId) {
        return cvMetadataRepository.findByStudentId(studentId);
    }

    public CVMetadata getCVById(Long id) {
        return cvMetadataRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CV not found: " + id));
    }

    public byte[] getCVFile(Long id) throws IOException {
        CVMetadata metadata = getCVById(id);
        Path path = Paths.get(metadata.getFilePath());
        return Files.readAllBytes(path);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            if (!contentType.startsWith("application/")) {
                throw new IllegalArgumentException("Invalid file type. Allowed: PDF, DOC, DOCX");
            }
        }
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
