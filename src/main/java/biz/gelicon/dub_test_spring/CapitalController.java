package biz.gelicon.dub_test_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CapitalController {

    List<DatebaseConn> datebaseConnList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(CapitalController.class);

    @RequestMapping(value = "/capital")
    public String edizmController(
            @RequestParam(name = "alias", required = false, defaultValue = "PS_DEVELOP_TRUNK") String alias,
            Model model
    ) {
        logger.info("Capital start. " + alias);
        // Заполним массив алиасов баз данных, пока просто вручную
        datebaseConnList.add(new DatebaseConn(
                "PS_DEVELOP_TRUNK",
                "org.postgresql.Driver",
                "jdbc:postgresql://10.15.3.39:5432/PS_DEVELOP_TRUNK",
                "SYSDBA",
                "masterkey"
        ));
        // Найдем первый совпадающий с алиасом и присвоим его глобальной переменной
        DatebaseConn.datebaseConn = datebaseConnList.stream()
                .filter(a -> a.alias.equals(alias))
                .findFirst()
                .orElse(null);
        if (DatebaseConn.datebaseConn == null) {
            throw new RuntimeException("База данныйх не найдена");
        }
        // Присвоим глобальную переменную соединившись с бд
        DatebaseConn.connection = DatebaseConn.datebaseConn.checkConnection(null);

        return "capital";
    }
}
