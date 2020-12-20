package biz.gelicon.dub_test_spring.controllers;

import biz.gelicon.dub_test_spring.DubTestSpringApplication;
import biz.gelicon.dub_test_spring.model.Edizm;
import biz.gelicon.dub_test_spring.repository.EdizmRepositoryJdbc;
import biz.gelicon.dub_test_spring.utils.DatebaseConn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private EdizmRepositoryJdbc edizmRepositoryJdbc;

    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный метод
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
    public String edizm(
            /* пары ключ-значение для передачи данных из Java кода в html страницы */
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "name_mask", required = false) String name_mask
    ) {
        logger.info("edizm - Start");
        List<Edizm> edizmList = new ArrayList<>();
        int masterCount = 0;
        if (true) {
            // делаем через spring
            if(page==null) page=0;
            if (name_mask == null) {
                edizmList = edizmRepositoryJdbc.findAll();
            } else {
                edizmList = edizmRepositoryJdbc.findByName(name_mask);
            }
            masterCount = edizmList.size();
            masterCount = edizmRepositoryJdbc.count();
            String alias = context.getEnvironment().getProperty("spring.datasource.url");
            model.addAttribute("alias", alias);
        } else {
            // делаем по старинке
            // Считаем глобальную переменную
            //connection = DatebaseConn.connection;
            // Перепроверим коннект
            connection = DatebaseConn.datebaseConn.checkConnection(DatebaseConn.connection);

            // Считаем единицы измерений
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
            model.addAttribute("alias", DatebaseConn.datebaseConn.alias);
        }
        // Передадим в модель
        model.addAttribute("edizmlist", edizmList);
        model.addAttribute("masterCount", masterCount);

        logger.info("edizmController - Finish");
        return "edizm/edizm"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }

    @RequestMapping(value = "/edizm_add")
    public String add(
            /* пары ключ-значение для передачи данных из Java кода в html страницы */
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "name_mask", required = false) String name_mask
    ) {
        logger.info("add - Start");
        Edizm edizm = new Edizm();
        // Для пробы
        edizm.id = 123;
        edizm.name = "Наименование";
        edizm.notation = "Обозначение";
        edizm.code = "Код";
        model.addAttribute("edizm", edizm);
        model.addAttribute("blockflagb", edizm.getBlockflagB());
        logger.info("add - Finish");
        return "edizm/edizm_add";
    }

    @RequestMapping(value = "/insertedizm")
    public String insertedizm(
            @Valid @ModelAttribute Edizm edizm,
            Model model
    ) {
        logger.info("Inserting " + edizm.toString());
        return "redirect:/edizm";
    }


}
