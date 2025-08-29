package com.mertdev.mirror_acoustics.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mertdev.mirror_acoustics.repository.ProductRepository;

@Controller
public class SitemapController {
    private final ProductRepository repo;
    @Value("${site.base-url}")
    String base;

    public SitemapController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        sb.append(url(base + "/"));
        sb.append(url(base + "/urun"));
        repo.findAll().forEach(p -> sb.append(url(base + "/urun/" + p.getSlug())));
        sb.append("</urlset>");
        return sb.toString();
    }

    private String url(String loc) {
        return "<url><loc>" + loc + "</loc></url>\n";
    }
}