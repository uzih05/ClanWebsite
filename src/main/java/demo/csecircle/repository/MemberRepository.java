package demo.csecircle.repository;

import demo.csecircle.classification.MemberRole;
import demo.csecircle.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String username);

    long countByMemberRole(MemberRole role);

    @Query("SELECT m FROM Member m WHERE " +
            "(:search IS NULL OR m.name LIKE %:search% OR m.email LIKE %:search% OR m.loginId LIKE %:search%) " +
            "AND (:role IS NULL OR m.memberRole = :role)")
    Page<Member> findMembersForManagement(String search, MemberRole role, Pageable pageable);

    Member findByEmail(String email);

    Member findMemberByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

}
