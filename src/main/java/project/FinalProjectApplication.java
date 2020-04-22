package project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import project.utils.DataInitializer;


@SpringBootApplication
public class FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(final DataInitializer initializer) {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
				initializer.initData();
			}
		};
	}

}
