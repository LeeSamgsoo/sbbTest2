package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void create(String username, String password, String nickname) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setNickname(nickname);
        this.userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        return this.userRepository.findByusername(username).orElse(null);
    }
}
