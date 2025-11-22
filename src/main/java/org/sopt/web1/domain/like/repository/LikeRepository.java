package org.sopt.web1.domain.like.repository;

import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    int countLikeByVideo(Video video);
}
