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

@Controller  /* Аннотация @Controller - говорит о том, что данный класс является контроллером.
                Контроллеры в Spring обрабатывают HTTP запросы на определенный адреса. */
public class EdizmController {
    private static final Logger logger = LoggerFactory.getLogger(EdizmController.class);

    @RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный метод
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
    public String edizmController(
            @RequestParam(name = "driverclassname", required = false,  defaultValue = "org.postgresql.Driver") String driverClassName,
                /* Аннотация @RequestParam говорит о том, что параметр String driverName - является параметром url
                   имеет имя driver, не обязательный, если не указан - postgresql
                   вызывается так http://localhost:8080/edizm?driver=postgresql */
            @RequestParam(name = "jdbcurl", required = false,  defaultValue = "10.15.3.39") String jdbcUrl,
            @RequestParam(name = "user", required = false,  defaultValue = "SYSDBA") String userName,
            @RequestParam(name = "password", required = false,  defaultValue = "masterkey") String passWord,
            Model model  /* пары ключ-значение
                            С помощью данного параметра мы можем передавать данные из Java кода в html страницы */
    ) {
        logger.info("edizmController - Start");
        model.addAttribute("driverclassname", driverClassName); /* Передаем в html имя хоста, полученное из html запроса
                                                                   в html извлекать так (на примере тега p):
                                                                   <p th:text="'Имя хоста: ' + ${host}" />
                                                                   Атрибут th у тега p - это инструмент движка шаблонов Thymeleaf
                                                                 */
        model.addAttribute("jdbcurl", jdbcUrl);
        model.addAttribute("user", userName);
        model.addAttribute("password", passWord);
        logger.info("Loading datebase driver {}.", driverClassName);

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Database provider class not found.", cnfe);
            return null;
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, userName, passWord);
        } catch (SQLException throwables) {
            logger.error("Database connection error.", throwables);
            return null;
        }

        logger.info("edizmController - Finish");
        return "edizm_null"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }
}
