package org.cobee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CobeeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CobeeServerApplication.class, args);
	}

}
