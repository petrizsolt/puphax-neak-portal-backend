package hu.neak.puphax.syncronizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class SyncronizerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SyncronizerApplication.class, args);
	}
	


}
