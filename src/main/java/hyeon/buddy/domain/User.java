package hyeon.buddy.domain;

import hyeon.buddy.enums.UserRole;
import hyeon.buddy.enums.UserStatus;
import jakarta.persistence.*;
import lombok.ToString;

@Entity
@Table(name = "user")
@ToString(callSuper = true)
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}
