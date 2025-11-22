package org.sopt.web1.domain.like.repository;

import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    int countLikeByVideo(Video video);

    boolean existsLikesByMemberAndVideo(Member member, Video video);

    Optional<Like> findLikeByMemberAndVideo(Member member, Video video);
}
