package biz.gelicon.dub_test_spring;

import biz.gelicon.dub_test_spring.repository.TowntypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication    /* Точка входа в приложение весенней загрузки — класс,
                             содержащий аннотацию @SpringBootApplication и метод main.
                             Spring Boot автоматически сканирует все компоненты,
                             включенные в проект, используя аннотацию @ComponentScan */
public class DubTestSpringApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DubTestSpringApplication.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private TowntypeRepository towntypeRepository;

	@Autowired
	@Qualifier("JdbcEdizmRepository")
	//@Qualifier("NamedParameterJdbcEdizmRepository")
	//private EdizmRepository edizmRepository;


	public static void main(String[] args) {
		SpringApplication.run(DubTestSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("StartApplication...");
		testTowntype();

	}

	private void testTowntype(){
		logger.info("testTowntype...Start");
		logger.info("delete={}",towntypeRepository.delete(200));
		logger.info("count={}",towntypeRepository.count());
		logger.info("insert={}",towntypeRepository.insert(new Towntype(200,"ЗОНА","Зона")));
		logger.info("update={}",towntypeRepository.update(new Towntype(200,"ЛАГЕРЬ","Лагерь")));
		logger.info("findAll:");
		towntypeRepository.findAll().forEach(t -> logger.info(t.toString()));
		String s = "З";
		logger.info("findByName: {}", s);
		towntypeRepository.findByName(s).forEach(t -> logger.info(t.toString()));
		Towntype towntype = towntypeRepository.findById(200).orElse(null);
		logger.info("findById={}",towntype.toString());
		int id = 2001;
		logger.info("getNameById({}) = {}",id,towntypeRepository.getNameById(id));
		id = 200;
		logger.info("getNameById({}) = {}",id,towntypeRepository.getNameById(id));

		logger.info("delete={}",towntypeRepository.delete(200));
	}
}
