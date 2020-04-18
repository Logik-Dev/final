package project;


import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import lombok.extern.slf4j.Slf4j;
import project.utils.DataInitializer;

@Slf4j
@SpringBootApplication
public class FinalProjectApplication {
	//private final Logger logger = LoggerFactory.getLogger(FinalProjectApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}
	
	
    @Bean
    CommandLineRunner initDatabase(final DataInitializer initializer) {
        return new CommandLineRunner() {
            @Override
            public void run(String... arg0) throws Exception {
                log.info(
                        "\n ******** Initializing Data ***********");
                initializer.initData();

                log.info(
                        "\n ******** Data initialized ***********");
            }
        };
    }
    
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.FRANCE);
	    return slr;
	}
	
}
