package com.mertdev.mirror_acoustics.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

public final class Slugger {
    public static String slugify(String s) {
        String base = Normalizer.normalize(Objects.toString(s, ""), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .trim().toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "-");
        return base;
    }
}
