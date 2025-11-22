package org.sopt.web1.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.member.service.MemberService;
import org.sopt.web1.domain.video.entity.Video;
import org.sopt.web1.domain.video.service.VideoService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeFacade {

    private final LikeService likeService;
    private final VideoService videoService;
    private final MemberService memberService;

    @Transactional
    public void likeVideo(Long memberId, Long videoId) {

        Member member = memberService.getMember(memberId);
        Video video = videoService.getVideoByVideoId(videoId);

        likeService.changeLike(member, video);
    }
}
