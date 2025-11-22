package org.sopt.web1.global.common.util;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    public String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileException(ErrorCode.FILE_INPUT_ERROR);
        }

        // MIME 타입 검사
        if (!file.getContentType().startsWith("video")) {
            throw new FileException(ErrorCode.FILE_TYPE_ERROR);
        }

        // 파일 크기 제한 5MB
        long MAX_SIZE = 5 * 1024 * 1024;
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("파일 크기는 5MB를 초과할 수 없습니다.");
        }

        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String key = UUID.randomUUID() + ext;

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .contentLength(file.getSize())
                        .build(),

                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}