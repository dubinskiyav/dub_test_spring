package biz.gelicon.dub_test_spring;

import biz.gelicon.dub_test_spring.repository.EdizmRepository;
import biz.gelicon.dub_test_spring.repository.JdbcEdizmRepository;
import biz.gelicon.dub_test_spring.repository.TowntypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

public class StartApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private EdizmRepository edizmRepository;

    //@Autowired
    private TowntypeRepository towntypeRepository;

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("StartApplication...");
        runJDBC();
    }

    private void runJDBC() {
        if (true) {return;}

        Integer i = towntypeRepository.count();
        logger.info("Saving...{}",i);


        List<Edizm> edizmList = Arrays.asList(
                new Edizm(1, "Ампер", "А", 0, "а"),
                new Edizm(2, "Вольт", "В", 0, "в"),
                new Edizm(3, "Люмен", "Л", 0, "лм"),
                new Edizm(4, "Фунт", "Ф", 0, "ф")
        );
        logger.info("[SAVE]");
        edizmList.forEach(edizm -> {
            logger.info("Saving...{}", edizm.toString());
            edizmRepository.save(edizm);
        });

        logger.info("[COUNT] Total edizms: {}", edizmRepository.count());

        logger.info("[FIND_ALL] {}", edizmRepository.findAll());

        int edizm_id = 2;
        logger.info("[FIND_BY_ID] :{}", edizm_id);
        Edizm edizm = edizmRepository.findById(edizm_id).orElseThrow(IllegalArgumentException::new);
        logger.info("{}", edizm);

        String s = "а";
        logger.info("[FIND_BY_NAME] : like '%{}%'", s);
        logger.info("{}", edizmRepository.findByName(s));

        logger.info("[GET_NAME_BY_ID] :{} = {}", edizm_id, edizmRepository.getNameById(edizm_id));

        logger.info("[UPDATE] :{}", edizm_id);
        edizm = new Edizm(2, "Вольтт", "ВВ", 1, "вв");
        logger.info("rows affected: {}", edizmRepository.update(edizm));

        logger.info("[DELETE] {}", edizm_id);
        logger.info("rows affected: {}", edizmRepository.deleteById(edizm_id));

        logger.info("[FIND_ALL] {}", edizmRepository.findAll());

    }
}
