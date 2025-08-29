package com.mertdev.mirror_acoustics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.mertdev.mirror_acoustics.domain")
@EnableJpaRepositories(basePackages = "com.mertdev.mirror_acoustics.repository")
public class MirrorAcousticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MirrorAcousticsApplication.class, args);
	}

}
