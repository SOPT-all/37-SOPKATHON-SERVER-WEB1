package org.sopt.web1.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.sopt.web1.domain.like.repository.LikeRepository;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public int countLikeByVideoId(Video video) {
        return likeRepository.countLikeByVideo(video);
    }
}
