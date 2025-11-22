package org.sopt.web1.domain.video.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.video.dto.VideoFeedListResponse;
import org.sopt.web1.domain.video.dto.VideoProjection;
import org.sopt.web1.domain.video.entity.Video;
import org.sopt.web1.domain.video.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;

    @Transactional
    public Video saveVideo(String videoUrl, int score, String content, Member member) {

        return videoRepository.save(Video.builder()
                .videoUrl(videoUrl)
                .score(score)
                .content(content)
                .member(member)
                .build());
    }

    public VideoFeedListResponse getFeedVideos() {

        List<VideoProjection> rows = videoRepository.findAllSorted();

        for (VideoProjection videoProjection : rows) {
            System.out.println(videoProjection.getFinalScore());
        }

        List<VideoFeedListResponse.VideoFeedItemResponse> items =
                rows.stream()
                        .map(r -> new VideoFeedListResponse.VideoFeedItemResponse(
                                r.getMemberId(),
                                r.getNickname(),
                                r.getVideoId(),
                                r.getThumbnailUrl(),
                                r.getLikeCount()
                        ))
                        .toList();

        return new VideoFeedListResponse(items);
    }
}
