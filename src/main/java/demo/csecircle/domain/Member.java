package demo.csecircle.domain;

import demo.csecircle.classification.Circle;
import demo.csecircle.classification.Major;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String nickname;

    @Column(unique = true)
    private String loginId;

    private String password;

    @Column(unique = true)
    private String email; // 학교메일만 허용

    private int sex; //성별 남자=0 여자=1

    @Enumerated(EnumType.STRING)
    private Major major; //학과

    private int grade; //학년

    @ManyToOne
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @ManyToOne
    @JoinColumn(name = "signup_clan_id")
    private Clan signupClan;
}
