package org.sopt.web1.domain.video.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.like.service.LikeService;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.video.dto.VideoFeedListResponse;
import org.sopt.web1.domain.video.dto.VideoProjection;
import org.sopt.web1.domain.member.service.MemberService;
import org.sopt.web1.domain.video.dto.VideoResponse;
import org.sopt.web1.domain.video.entity.Video;
import org.sopt.web1.domain.video.repository.VideoRepository;
import org.sopt.web1.global.exception.ErrorCode;
import org.sopt.web1.global.exception.handler.VideoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberService memberService;
    private final LikeService likeService;

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

    @Transactional
    public void deleteVideo(Long memberId, Long videoId) {

        memberService.getMember(memberId);
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(ErrorCode.NOT_FOUND_VIDEO));

        videoRepository.delete(video);
    }

    public VideoResponse getVideo(Long memberId, Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(ErrorCode.NOT_FOUND_VIDEO));

        Member member = video.getMember();
        Member loginedMember = memberService.getMember(memberId);

        int likeCount = likeService.countLikeByVideoId(video);
        boolean isLiked = likeService.existsLikesByMemberAndVideo(loginedMember, video);

        return new VideoResponse(
                member.getMemberId(),
                member.getNickname(),
                video.getVideoUrl(),
                video.getThumbnailUrl(),
                isLiked,
                likeCount,
                video.getContent(),
                video.getScore()
        );
    }

    // 내부 메서드
    public Video getVideoByVideoId(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoException(ErrorCode.NOT_FOUND_VIDEO));

        return video;
    }
}
