package org.sopt.web1.domain.like.repository;

import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    int countLikeByVideo(Video video);

    @Query("select distinct l.video from Like l where l.member = :member")
    List<Video> findLikedVideos(@Param("member") Member member);

    Integer countByVideo(Video v);
}
