package biz.gelicon.dub_test_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        connection = DatebaseConn.connection;

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
        // Передадим в модель
        model.addAttribute("edizmlist", edizmList);
        model.addAttribute("alias", DatebaseConn.datebaseConn.alias);



        logger.info("edizmController - Finish");
        return "edizm"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }

    // Обязательно public иначе в шаблоне не увидит!!!! и поля тоже public !!!!
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
