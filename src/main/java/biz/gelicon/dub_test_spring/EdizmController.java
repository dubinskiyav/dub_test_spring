package biz.gelicon.dub_test_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller  /* Аннотация @Controller - говорит о том, что данный класс является контроллером.
                Контроллеры в Spring обрабатывают HTTP запросы на определенный адреса. */
public class EdizmController {

    private static final Logger logger = LoggerFactory.getLogger(EdizmController.class);
    Connection connection;

    @RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный метод
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
    public String edizmController(
            Model model  /* пары ключ-значение
                            С помощью данного параметра мы можем передавать данные из Java кода в html страницы */
    ) {
        logger.info("edizmController - Start");

        // Считаем глобальную переменную
        //connection = DatebaseConn.connection;
        // Перепроверим коннект
        connection = DatebaseConn.datebaseConn.checkConnection(DatebaseConn.connection);

        // Считаем единицы измерений
        List<Edizm> edizmList = new ArrayList<>();
        int masterCount = 0;
        // Простой цикл
        try {
            PreparedStatement ps = null;
            if (true) {
                ps = connection.prepareStatement(""
                        + " SELECT E.edizm_id, "
                        + "        E.edizm_name, "
                        + "        E.edizm_notation, "
                        + "        E.edizm_blockflag, "
                        + "        E.edizm_code "
                        + " FROM   edizm E"
                        + " WHERE  E.edizm_id != ? "
                        + " ORDER BY E.edizm_id ");
                ps.setInt(1, -123);
            } else {
                // Много записей
                ps = connection.prepareStatement(""
                        + " SELECT E.edizm_id, "
                        + "        E.edizm_name, "
                        + "        E.edizm_notation, "
                        + "        E.edizm_blockflag, "
                        + "        E.edizm_code "
                        + " FROM   edizm E,  "
                        + "        edizm E1, "
                        + "        edizm E2,"
                        + "        edizm E3,"
                        + "        edizm E4,"
                        + "        edizm E5  "
                        + " WHERE  E.edizm_id != ? "
                        + " ORDER BY E5.edizm_id,"
                        + "          E4.edizm_id,"
                        + "          E3.edizm_id, "
                        + "          E2.edizm_id,"
                        + "          E1.edizm_id,"
                        + "          E.edizm_id ");
                ps.setInt(1, -123);
            }
            logger.info("executeQuery Start");
            ResultSet rs = ps.executeQuery();
            logger.info("executeQuery Finish");
            while (rs.next()) {
                edizmList.add(new Edizm(
                        rs.getInt("edizm_id"),
                        rs.getString("edizm_name"),
                        rs.getString("edizm_notation"),
                        rs.getInt("edizm_blockflag"),
                        rs.getString("edizm_code")
                ));
                masterCount++;
            }
            logger.info("next Finish");
            ps.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        // Передадим в модель
        model.addAttribute("edizmlist", edizmList);
        model.addAttribute("masterCount", masterCount);
        model.addAttribute("alias", DatebaseConn.datebaseConn.alias);

        logger.info("edizmController - Finish");
        return "edizm"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }

}
