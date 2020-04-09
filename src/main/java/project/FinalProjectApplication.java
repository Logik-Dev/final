package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalProjectApplication {
	
	//private final Logger logger = LoggerFactory.getLogger(FinalProjectApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}
	
	/**
    @Bean
    CommandLineRunner initDatabase(final DataInitializer initializer) {
        return new CommandLineRunner() {
            @Override
            public void run(String... arg0) throws Exception {
                logger.info(
                        "\n ******** Initializing Data ***********");
                initializer.initData();

                logger.info(
                        "\n ******** Data initialized ***********");
            }
        };
    }
    */
	
}
