package hyeon.buddy.domain;

import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.enums.UserRole;
import hyeon.buddy.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Table(name = "user")
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public static User from(UserSignUpRequestDTO dto){
        return User.builder()
                .account(dto.getAccount())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userRole(UserRole.ROLE_USER)
                .userStatus(UserStatus.UNVERIFIED)
                .build();

    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setPassword(String encodePassword) {
        this.password = encodePassword;
    }
}