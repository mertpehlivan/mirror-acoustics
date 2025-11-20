package com.mertdev.mirror_acoustics.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "showcase_images")
@Data
@NoArgsConstructor
public class ShowcaseImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url; // /uploads/...

    /**
     * Type of showcase entry: "image" or "video". Default is "image" for backward compatibility.
     */
    private String type = "image";

    /**
     * If type is "video", this is the video URL (can be local or external).
     */
    private String videoUrl;

    private String titleTr;
    private String titleEn;

    private Integer sortOrder = 0;

    private boolean active = true;

    /**
     * How the image should be displayed inside the frame. "cover" or "contain" (object-fit values).
     */
    private String displayMode = "cover";

    /**
     * Frame height in pixels. If null, UI default will be used.
     */
    private Integer frameHeight; // px

    /**
     * Object position inside the frame (CSS object-position). Examples: "center", "top", "bottom right", "25% 75%".
     */
    private String objectPosition = "center";

    private LocalDateTime createdAt = LocalDateTime.now();
}

