package univ.yesummit.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private Authority authority; //ADMIN, USER, SIGN_OUT

    private String provider;

    private String providerId;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    //== 회원 정보 수정 ==//
    public void updateUserName(String newUsername) {
        this.username = newUsername;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    //== 회원 가입 시에 USER권한을 부여 ==//
    public void addUserAuthority() {
        this.authority = Authority.USER;
    }


}
