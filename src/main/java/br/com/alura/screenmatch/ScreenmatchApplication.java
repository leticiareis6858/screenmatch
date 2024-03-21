package br.com.alura.screenmatch;

import br.com.alura.screenmatch.service.ConsumoAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI=new ConsumoAPI();
		var json=consumoAPI.obterDados("https://www.omdbapi.com/?t=doctor+who&apikey=194f8ef8");
		System.out.println(json);
	}
}
