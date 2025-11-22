package org.sopt.web1.domain.video.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.sopt.web1.domain.like.entity.Like;
import org.sopt.web1.domain.member.entity.Member;
import org.sopt.web1.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByMember(Member member);
}
