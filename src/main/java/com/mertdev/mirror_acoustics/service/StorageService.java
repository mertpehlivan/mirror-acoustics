package com.mertdev.mirror_acoustics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.showcase.image.width:1500}")
    private int showcaseTargetWidth;

    @Value("${app.showcase.image.height:1000}")
    private int showcaseTargetHeight;

    @Value("${app.showcase.image.quality:0.9}")
    private float showcaseJpegQuality;

    public String save(MultipartFile file) throws IOException {
        return saveInternal(file, false);
    }

    public String saveShowcaseImage(MultipartFile file) throws IOException {
        return saveInternal(file, true);
    }

    private String saveInternal(MultipartFile file, boolean processShowcase) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Files.createDirectories(Path.of(uploadDir));

        if (!processShowcase) {
            String filename = buildFilename(file, false, null);
            Path target = Path.of(uploadDir, filename);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/" + filename;
        }

        // For showcase images we now preserve the original uploaded file (no automatic crop/resize)
        String filename = buildFilename(file, false, null);
        Path target = Path.of(uploadDir, filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/uploads/" + filename;
    }

    private void writeImage(BufferedImage image, Path target, String format) throws IOException {
        Files.createDirectories(target.getParent());
        if ("jpg".equals(format) || "jpeg".equals(format)) {
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgbImage.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(image, 0, 0, Color.WHITE, null);
            } finally {
                g.dispose();
            }

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) {
                try (OutputStream output = Files.newOutputStream(target)) {
                    ImageIO.write(rgbImage, "jpg", output);
                }
                return;
            }

            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(Math.max(0f, Math.min(showcaseJpegQuality, 1f)));
            }

            try (OutputStream output = Files.newOutputStream(target);
                 ImageOutputStream ios = ImageIO.createImageOutputStream(output)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(rgbImage, null, null), param);
            } finally {
                writer.dispose();
            }
        } else {
            try (OutputStream output = Files.newOutputStream(target)) {
                ImageIO.write(image, format, output);
            }
        }
    }

    private BufferedImage cropAndResize(BufferedImage original, int targetWidth, int targetHeight) {
        double targetRatio = (double) targetWidth / (double) targetHeight;
        int width = original.getWidth();
        int height = original.getHeight();
        double sourceRatio = width / (double) height;

        int cropWidth = width;
        int cropHeight = height;
        int x = 0;
        int y = 0;

        if (sourceRatio > targetRatio) {
            cropWidth = (int) Math.round(height * targetRatio);
            cropWidth = Math.min(cropWidth, width);
            x = (width - cropWidth) / 2;
        } else if (sourceRatio < targetRatio) {
            cropHeight = (int) Math.round(width / targetRatio);
            cropHeight = Math.min(cropHeight, height);
            y = (height - cropHeight) / 2;
        }

        cropWidth = Math.max(1, cropWidth);
        cropHeight = Math.max(1, cropHeight);
        x = Math.max(0, Math.min(x, width - cropWidth));
        y = Math.max(0, Math.min(y, height - cropHeight));

        BufferedImage cropped = original.getSubimage(x, y, cropWidth, cropHeight);

        int imageType = cropped.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage output = new BufferedImage(targetWidth, targetHeight, imageType);
        Graphics2D g2d = output.createGraphics();
        try {
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(cropped, 0, 0, targetWidth, targetHeight, null);
        } finally {
            g2d.dispose();
        }
        return output;
    }

    private String buildFilename(MultipartFile file, boolean overrideExtension, String extension) {
        String original = Objects.requireNonNullElse(file.getOriginalFilename(), "file");
        original = original.replace("\\", "_").replace("/", "_");
        original = original.replaceAll("\s", "_");
        if (overrideExtension) {
            original = stripExtension(original) + "." + extension;
        } else if (!original.contains(".")) {
            original = original + (extension != null ? "." + extension : ".bin");
        }
        if (original.startsWith(".")) {
            original = "file" + original;
        }
        if (original.isBlank()) {
            original = "file." + (extension != null ? extension : "bin");
        }
        return UUID.randomUUID() + "-" + original;
    }

    private String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot <= 0) {
            return name;
        }
        return name.substring(0, dot);
    }

    private String determineTargetFormat(String originalName) {
        String ext = "";
        if (originalName != null) {
            int dot = originalName.lastIndexOf('.');
            if (dot >= 0 && dot < originalName.length() - 1) {
                ext = originalName.substring(dot + 1).toLowerCase(Locale.ROOT);
            }
        }
        if ("png".equals(ext)) {
            return "png";
        }
        return "jpg";
    }
}
