package biz.gelicon.dub_test_spring;

import biz.gelicon.dub_test_spring.model.Edizm;
import biz.gelicon.dub_test_spring.model.Towntype;
import biz.gelicon.dub_test_spring.repository.EdizmRepository;
import biz.gelicon.dub_test_spring.repository.TowntypeRepository;
import biz.gelicon.dub_test_spring.utils.DatebaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication    /* Точка входа в приложение весенней загрузки — класс,
                             содержащий аннотацию @SpringBootApplication и метод main.
                             Spring Boot автоматически сканирует все компоненты,
                             включенные в проект, используя аннотацию @ComponentScan */
/* Указать Spring, где искать аннотированные классы */
/*
@ComponentScan(basePackages = {
        "biz.gelicon.dub_test_spring.repository",
        "biz.gelicon.dub_test_spring.controllers"})
 */
public class DubTestSpringApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DubTestSpringApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Autowired
    private TowntypeRepository towntypeRepository;

    @Autowired
    private EdizmRepository edizmRepository;

    public static void main(String[] args) {
        SpringApplication.run(DubTestSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("StartApplication...");
        if (false) {
            testEdizm();
            testTowntype();
        }
        DatebaseUtils.setDbType(jdbcTemplate);
    }

    private void testEdizm() {
        logger.info("testEdizm...Start");
        List<Edizm> edizmList = Arrays.asList(
                new Edizm(1, "Ампер", "А", 0, "а"),
                new Edizm(2, "Вольт", "В", 0, "в"),
                new Edizm(3, "Люмен", "Л", 0, "лм"),
                new Edizm(4, "Фунт", "Ф", 0, "ф")
        );
        edizmList.stream().map(e -> e.getId())
                .forEach(i -> logger.info("delete={}", edizmRepository.delete(i)));
        logger.info("insert");
        edizmList.forEach(edizm -> {
            logger.info("insert...{}", edizm.toString());
            edizmRepository.insert(edizm);
        });
        logger.info("count={}", edizmRepository.count());
        logger.info("update={}", edizmRepository.update(new Edizm(4, "Фут", "ФТ", 0, "фт")));
        logger.info("findAll:");
        edizmRepository.findAll().forEach(t -> logger.info(t.toString()));
        String s = "а";
        logger.info("findByName: {}", s);
        edizmRepository.findByName(s).forEach(t -> logger.info(t.toString()));
        int i = 1355549;
        logger.info("findById({})...", i);
        Edizm edizm = edizmRepository.findById(i);
        if (edizm != null) {
            logger.info(edizm.toString());
            logger.info("findById({})={}", i, edizm.toString());
        } else {
            logger.info("Не нашли с id={}", i);
        }
        int id = 2001;
        logger.info("getNameById({}) = {}", id, edizmRepository.getNameById(id));
        id = 2;
        logger.info("getNameById({}) = {}", id, edizmRepository.getNameById(id));
        edizmList.stream().map(e -> e.getId())
                .forEach(e -> logger.info("delete={}", edizmRepository.delete(e)));
    }

    private void testTowntype() {
        logger.info("testTowntype...Start");
        logger.info("delete={}", towntypeRepository.delete(200));
        logger.info("count={}", towntypeRepository.count());
        logger.info("insert={}", towntypeRepository.insert(new Towntype(200, "ЗОНА", "Зона")));
        logger.info("update={}", towntypeRepository.update(new Towntype(200, "ЛАГЕРЬ", "Лагерь")));
        logger.info("findAll:");
        towntypeRepository.findAll().forEach(t -> logger.info(t.toString()));
        String s = "З";
        logger.info("findByName: {}", s);
        towntypeRepository.findByName(s).forEach(t -> logger.info(t.toString()));
        Towntype towntype = towntypeRepository.findById(200).orElse(null);
        logger.info("findById={}", towntype.toString());
        int id = 2001;
        logger.info("getNameById({}) = {}", id, towntypeRepository.getNameById(id));
        id = 200;
        logger.info("getNameById({}) = {}", id, towntypeRepository.getNameById(id));

        logger.info("delete={}", towntypeRepository.delete(200));
    }
}

