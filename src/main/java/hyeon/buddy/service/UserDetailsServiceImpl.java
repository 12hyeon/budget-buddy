package hyeon.buddy.service;

import hyeon.buddy.domain.User;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        List<GrantedAuthority> authorities = user.getUserRole().getAuthorities();

        return org.springframework.security.core.userdetails.User
                .withUsername(String.valueOf(user.getId()))
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
