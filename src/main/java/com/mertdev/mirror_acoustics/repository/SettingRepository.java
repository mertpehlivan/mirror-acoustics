package com.mertdev.mirror_acoustics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mertdev.mirror_acoustics.domain.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByKey(String key);
}

