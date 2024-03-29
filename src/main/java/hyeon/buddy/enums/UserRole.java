package hyeon.buddy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_USER("ROLE_USER", "USER"),
    ROLE_ADMIN("ROLE_ADMIN", "ADMIN");

    private final String name;
    private final String authority;

    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }
}
