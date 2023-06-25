package teo.sprint.navogue.common.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UsernameNotFoundException("조회된 회원이 없습니다."));

        return new AuthUser(user.getId(), user.getEmail());
    }
}
