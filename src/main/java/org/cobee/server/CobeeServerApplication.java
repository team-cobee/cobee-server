package org.cobee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
@EnableAsync
@EnableJpaAuditing
public class CobeeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CobeeServerApplication.class, args);
	}

}
