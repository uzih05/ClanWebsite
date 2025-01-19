package demo.csecircle.domain;

import demo.csecircle.classification.ClanRecruit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Clan {

    @Id
    @GeneratedValue
    @Column(name = "clan_id")
    private Long id;

    private String leaderName;

    @Enumerated(EnumType.STRING)
    private ClanRecruit isRecruit;

    @Column(unique = true)
    private String clanName;

    @Lob
    private String description;

    private String meetingTime;

    private String clanLocation;

    private String telNum;

    @OneToMany(mappedBy = "clan")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "signupClan")
    private List<Member> signupMembers = new ArrayList<>();

    @Lob
    private byte[] image; // 이미지 데이터를 저장 (byte 배열)

    // 문서 파일을 저장할 필드
    @Lob
    private byte[] document;
}
