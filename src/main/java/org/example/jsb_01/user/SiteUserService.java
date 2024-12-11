package org.example.jsb_01.user;

import lombok.RequiredArgsConstructor;
import org.example.jsb_01.DataNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    public void createSiteUser(String username, String password, String email) {
        SiteUserDto.toDto(siteUserRepository.save(
            SiteUser.create(username, passwordEncoder.encode(password), email)));
    }

    public SiteUserDto getSiteUser(String username) {
        return SiteUserDto.toDto(siteUserRepository.findByUsername(username)
            .orElseThrow(() -> new DataNotFoundException("user not found")));
    }

}
