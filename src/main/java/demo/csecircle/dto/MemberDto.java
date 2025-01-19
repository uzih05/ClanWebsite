package demo.csecircle.dto;

import demo.csecircle.domain.Member;
import lombok.Data;

@Data
public class MemberDto {
    private Member member;
    private String base64Image;

    public MemberDto(Member member, String base64Image) {
        this.member = member;
        this.base64Image = base64Image;
    }
}
