package org.sopt.web1.domain.video.repository;

import org.sopt.web1.domain.video.dto.VideoProjection;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("""
        SELECT
            v.videoId AS videoId,
            m.memberId AS memberId,
            m.nickname AS nickname,
            v.thumbnailUrl AS thumbnailUrl,
            COUNT(l.likeId) AS likeCount,
            v.score AS score,
            (
              (COUNT(l.likeId) * 0.5)
              +
              (v.score * 0.5)
            ) AS finalScore

        FROM Video v
        JOIN v.member m
        LEFT JOIN Like l ON l.video.videoId = v.videoId

        GROUP BY v.videoId, m.memberId, m.nickname, v.thumbnailUrl, v.score
        ORDER BY
            (
                (COUNT(l) * 0.5) + (v.score * 0.5)
            ) DESC
    """)
    List<VideoProjection> findAllSorted();
}
