package org.sopt.web1.domain.member.repository;

import jakarta.validation.constraints.NotBlank;
import org.sopt.web1.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
}
