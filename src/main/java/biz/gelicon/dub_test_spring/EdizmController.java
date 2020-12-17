package biz.gelicon.dub_test_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller  /* Аннотация @Controller - говорит о том, что данный класс является контроллером.
                Контроллеры в Spring обрабатывают HTTP запросы на определенный адреса. */
public class EdizmController {

    private static final Logger logger = LoggerFactory.getLogger(EdizmController.class);
    public Connection connection;

    @RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный метод
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
    public String edizmController(
            @RequestParam(name = "driverclassname", required = false, defaultValue = "org.postgresql.Driver") String driverClassName,
                /* Аннотация @RequestParam говорит о том, что параметр String driverName - является параметром url
                   имеет имя driver, не обязательный, если не указан - postgresql
                   вызывается так http://localhost:8080/edizm?driver=postgresql */
            @RequestParam(name = "jdbcurl", required = false, defaultValue = "10.15.3.39") String jdbcUrl,
            @RequestParam(name = "user", required = false, defaultValue = "SYSDBA") String userName,
            @RequestParam(name = "password", required = false, defaultValue = "masterkey") String passWord,
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
            connection = DriverManager.getConnection(jdbcUrl, userName, passWord);
        } catch (SQLException throwables) {
            logger.error("Database connection error.", throwables);
            return null;
        }
        logger.error("Database connected");
        // Считаем единицы измерений
        List<Edizm> edizmList = new ArrayList<>();
        // Простой цикл
        try {
            PreparedStatement ps = connection.prepareStatement(""
                    + " SELECT edizm_id, "
                    + "        edizm_name, "
                    + "        edizm_notation, "
                    + "        edizm_blockflag, "
                    + "        edizm_code "
                    + " FROM   edizm "
                    + " WHERE  edizm_id != ? "
                    + " ORDER BY edizm_id ");
            ps.setInt(1, -123);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                edizmList.add(new Edizm(
                        rs.getInt("edizm_id"),
                        rs.getString("edizm_name"),
                        rs.getString("edizm_notation"),
                        rs.getInt("edizm_blockflag"),
                        rs.getString("edizm_code")
                ));
            }
            ps.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        // Попробуем передать в модель
        model.addAttribute("edizmlist", edizmList);

        logger.info("edizmController - Finish");
        return "edizm_null"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }

    // Обязательно public иначе в шаблоне не увидит!!!! и поля тоже!!!!
    public class Edizm {

        public Integer id;
        public String name;
        public String notation;
        public Integer blockflag;
        public String code;

        Edizm(
                Integer id,
                String name,
                String notation,
                Integer blockflag,
                String code
        ) {
            this.id = id;
            this.name = name;
            this.notation = notation;
            this.blockflag = blockflag;
            this.code = code;
        }
        @Override
        public String toString() {
            return id.toString() + " " + name + " " + notation + " " + blockflag.toString() + " " + code;
        }
    }

}
