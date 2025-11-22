package org.sopt.web1.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.like.repository.LikeRepository;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.member.service.MemberService;
import org.sopt.web1.domain.video.entity.Video;
import org.sopt.web1.domain.video.repository.VideoRepository;
import org.sopt.web1.domain.video.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    public int countLikeByVideoId(Video video) {
        return likeRepository.countLikeByVideo(video);
    }

    public boolean existsLikesByMemberAndVideo(Member member, Video video) {
        return likeRepository.existsLikesByMemberAndVideo(member, video);
    }

    @Transactional
    public void changeLike(Member member, Video video) {
        Optional<Like> likeByMemberAndVideo = likeRepository.findLikeByMemberAndVideo(member, video);

        if (likeByMemberAndVideo.isPresent()) {
            likeRepository.delete(likeByMemberAndVideo.get());
            video.unlikeVideo();
        } else {
            Like like = Like.builder().video(video).member(member).build();
            likeRepository.save(like);
            video.likeVideo();
        }
    }
}
