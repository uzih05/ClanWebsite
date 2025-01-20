package demo.csecircle.service;

import demo.csecircle.classification.MemberRole;
import demo.csecircle.domain.Member;
import demo.csecircle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Page<Member> findMembers(String search, MemberRole role, Pageable pageable) {
        return memberRepository.findMembersForManagement(search, role, pageable);
    }

    public long countTotalMembers() {
        return memberRepository.count();
    }

    public long countMembersByRole(MemberRole role) {
        return memberRepository.countByMemberRole(role);
    }

    public void updateMemberRole(Long memberId, MemberRole role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.setMemberRole(role);
        memberRepository.save(member);
    }

    public void deleteMembers(Long[] memberIds) {
        for (Long id : memberIds) {
            memberRepository.deleteById(id);
        }
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}

