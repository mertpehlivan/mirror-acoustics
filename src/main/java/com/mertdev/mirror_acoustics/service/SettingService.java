package com.mertdev.mirror_acoustics.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mertdev.mirror_acoustics.domain.Setting;
import com.mertdev.mirror_acoustics.repository.SettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository repo;

    public String get(String key, String lang) {
        Optional<Setting> s = repo.findByKey(key);
        if (s.isEmpty()) {
            return "";
        }
        return "en".equals(lang) ? s.get().getValueEn() : s.get().getValueTr();
    }

    public Setting find(String key) {
        return repo.findByKey(key).orElseGet(() -> {
            Setting s = new Setting();
            s.setKey(key);
            return repo.save(s);
        });
    }

    public void save(String key, String valueTr, String valueEn) {
        Setting s = repo.findByKey(key).orElseGet(() -> {
            Setting n = new Setting();
            n.setKey(key);
            return n;
        });
        s.setValueTr(valueTr);
        s.setValueEn(valueEn);
        repo.save(s);
    }
}

