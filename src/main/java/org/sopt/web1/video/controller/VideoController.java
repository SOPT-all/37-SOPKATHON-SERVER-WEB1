package org.sopt.web1.video.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.global.common.response.ApiResponse;
import org.sopt.web1.global.common.util.S3Uploader;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.ServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/videos")
public class VideoController {

    private final S3Uploader s3Uploader;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> uploadAndStartAnalysis(
            @RequestParam("video") MultipartFile video,
            @RequestParam("tags") List<String> tags) {

        String s3Url;
        try {
            // 1. S3 업로드 요청
            s3Url = s3Uploader.upload(video);

        } catch (IllegalArgumentException e) {
            // S3Uploader에서 던져진 파일 유효성 오류 처리 (400 Bad Request)
            return ResponseEntity.badRequest().body(ApiResponse.fail(400, e.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        System.out.println("S3 URL: " + s3Url);
        return ResponseEntity.ok(ApiResponse.ok("동영상 업로드 및 분석 요청이 성공적으로 처리되었습니다. S3 URL: "));
    }
}
