package demo.csecircle.controller;

import demo.csecircle.UserConst;
import demo.csecircle.classification.MemberRole;
import demo.csecircle.domain.Member;
import demo.csecircle.dto.MemberDto;
import demo.csecircle.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/members")
public class AdminController {

    private final MemberService memberService;

    @GetMapping
    public String memberManagement(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) MemberRole role,
            @PageableDefault(size = 10) Pageable pageable,
            @SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member currentMember,
            Model model) {


//        if (currentMember == null || currentMember.getMemberRole() != MemberRole.WEBSITE_ADMIN) {
//            return "redirect:/";
//        }




        // 페이징된 멤버 리스트 가져오기
        Page<Member> members = memberService.findMembers(search, role, pageable);

        // 멤버 리스트를 DTO로 변환하여 base64Image 포함
        List<MemberDto> memberDTOs = members.stream()
                .map(member -> {
                    String base64Image = null;
                    if (member.getProfileImage() != null) {
                        base64Image = Base64.getEncoder().encodeToString(member.getProfileImage());
                    }
                    return new MemberDto(member, base64Image);
                })
                .collect(Collectors.toList());

        long totalElements = members.getTotalElements(); // 전체 데이터 개수 예시
        int pageSize = pageable.getPageSize(); // 한 페이지에 표시될 항목 수
        int currentPage = pageable.getPageNumber(); // 현재 페이지 번호

        // 필요한 계산 수행 (컨트롤러에서 처리)
        long result = Math.min((long) (currentPage + 1) * pageSize, totalElements);

        // 결과를 모델에 추가
        model.addAttribute("paginationResult", result);
        model.addAttribute("members", memberDTOs); // memberDTOs를 모델에 전달
        model.addAttribute("pagination", members);
        model.addAttribute("currentMember", currentMember);
        model.addAttribute("totalMembers", memberService.countTotalMembers());
        model.addAttribute("clubPresidents", memberService.countMembersByRole(MemberRole.CLUB_PRESIDENT));
        model.addAttribute("websiteAdmins", memberService.countMembersByRole(MemberRole.WEBSITE_ADMIN));
        model.addAttribute("roles", MemberRole.values());
        model.addAttribute("selectedRole", role);
        model.addAttribute("searchQuery", search);

        return "admin/management";

    }

    @PostMapping("/{id}/role")
    @ResponseBody
    public String updateMemberRole(
            @PathVariable Long id,
            @RequestParam MemberRole role,
            @SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member currentMember
    ) {
        if (currentMember == null || currentMember.getMemberRole() != MemberRole.WEBSITE_ADMIN) {
            return "Unauthorized";
        }

        memberService.updateMemberRole(id, role);
        return "Success";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteMembers(
            @RequestBody Long[] memberIds,
            @SessionAttribute(name = UserConst.LOGIN_MEMBER, required = false) Member currentMember
    ) {
        if (currentMember == null || currentMember.getMemberRole() != MemberRole.WEBSITE_ADMIN) {
            return "Unauthorized";
        }

        memberService.deleteMembers(memberIds);
        return "Success";
    }
}
