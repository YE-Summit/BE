package univ.yesummit.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    boolean existsByUsername(String username);
}
