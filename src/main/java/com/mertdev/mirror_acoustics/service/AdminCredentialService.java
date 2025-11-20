package com.mertdev.mirror_acoustics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mertdev.mirror_acoustics.domain.Setting;
import com.mertdev.mirror_acoustics.repository.SettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCredentialService implements UserDetailsService {

    private static final String ADMIN_PASSWORD_KEY = "admin.password.hash";

    private final SettingRepository settingRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.default-password:admin123}")
    private String defaultPassword;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!adminUsername.equals(username)) {
            throw new UsernameNotFoundException("Unknown user");
        }
        String passwordHash = resolvePasswordHash();
        return User.withUsername(adminUsername)
                .password(passwordHash)
                .roles("ADMIN")
                .build();
    }

    @Transactional
    public boolean matchesCurrentPassword(String rawPassword) {
        String passwordHash = resolvePasswordHash();
        return passwordEncoder.matches(rawPassword, passwordHash);
    }

    @Transactional
    public void updatePassword(String newPassword) {
        String passwordHash = passwordEncoder.encode(newPassword);
        Setting setting = settingRepository.findByKey(ADMIN_PASSWORD_KEY)
                .orElseGet(() -> {
                    Setting s = new Setting();
                    s.setKey(ADMIN_PASSWORD_KEY);
                    return s;
                });
        setting.setValueTr(passwordHash);
        setting.setValueEn(passwordHash);
        settingRepository.save(setting);
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    private String resolvePasswordHash() {
        return settingRepository.findByKey(ADMIN_PASSWORD_KEY)
                .map(Setting::getValueTr)
                .filter(StringUtils::hasText)
                .orElseGet(this::initializeDefaultPassword);
    }

    private String initializeDefaultPassword() {
        String hash = passwordEncoder.encode(defaultPassword);
        Setting setting = settingRepository.findByKey(ADMIN_PASSWORD_KEY)
                .orElseGet(() -> {
                    Setting s = new Setting();
                    s.setKey(ADMIN_PASSWORD_KEY);
                    return s;
                });
        setting.setValueTr(hash);
        setting.setValueEn(hash);
        settingRepository.save(setting);
        return hash;
    }
}
