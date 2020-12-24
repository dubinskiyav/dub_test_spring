package biz.gelicon.dub_test_spring.controllers;

import biz.gelicon.dub_test_spring.model.Edizm;
import biz.gelicon.dub_test_spring.repository.EdizmRepositoryJdbc;
import biz.gelicon.dub_test_spring.utils.DatebaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Controller  /* Аннотация @Controller - говорит о том, что данный класс является контроллером.
                Контроллеры в Spring обрабатывают HTTP запросы на определенный адреса. */
@RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный класс
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
public class EdizmController {

    private static final Logger logger = LoggerFactory.getLogger(EdizmController.class);
    Connection connection;

    @Autowired
    private EdizmRepositoryJdbc edizmRepositoryJdbc;

    @Autowired
    private ApplicationContext context;

    // Главная форма
    @RequestMapping(value = "index")
    public String edizm(
            /* пары ключ-значение для передачи данных из Java кода в html страницы */
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "name_mask", required = false) String name_mask
    ) {
        logger.info("edizm - Start");
        List<Edizm> edizmList = new ArrayList<>();
        int masterCount = 0;
        if (page == null) { page = 0; }
        if (name_mask == null) {
            edizmList = edizmRepositoryJdbc.findAll();
        } else {
            edizmList = edizmRepositoryJdbc.findByName(name_mask);
        }
        masterCount = edizmList.size();
        masterCount = edizmRepositoryJdbc.count();
        String alias = context.getEnvironment().getProperty("spring.datasource.url");
        model.addAttribute("alias", alias);
        model.addAttribute("edizmlist", edizmList);
        model.addAttribute("masterCount", masterCount);

        logger.info("edizmController - Finish");
        return "edizm/index"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }

    // Форма добавленя
    @RequestMapping(value = "add")
    public String add(
            /* пары ключ-значение для передачи данных из Java кода в html страницы */
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "name_mask", required = false) String name_mask
    ) {
        logger.info("add - Start");
        Edizm edizm = new Edizm();
        // Для пробы
        edizm.setBlockflagB(false);
        model.addAttribute("edizm", edizm);
        logger.info("add - Finish");
        return "edizm/form";
    }

    // Изменение
    @RequestMapping(value = "upd/{id}")
    @Transactional(propagation = Propagation.REQUIRED)
    public String upd(
            Model model,
            @PathVariable("id") Integer id
    ) {
        logger.info("upd - Start edizm_id = " + id);
        // Сляпаем из id
        Edizm edizm = edizmRepositoryJdbc.findById(id);
        model.addAttribute("edizm", edizm);
        logger.info("upd - Finish with result = 1");
        return "edizm/form";
    }


    // Множественное удаление
    @RequestMapping(value = "del_ids/{ids}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK) // todo Почему так?
    @Transactional(propagation = Propagation.REQUIRED)
    public void delIds(
            Model model,
            @PathVariable("ids") String ids
    ) {
        logger.info("delele (" + ids + ")");
        for (String s : ids.replaceAll("\\s+", "").split(",")) {
            Integer id = Integer.parseInt(s);
            Integer i = edizmRepositoryJdbc.delete(id);
        }
        logger.info("Deleted");
    }

    // Выполнение добавления или изменения
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRED)
    public String postEdizm(
            @Valid @ModelAttribute Edizm edizm,
            BindingResult result,
            Model model
    ) {
        logger.info("Saving... " + edizm.toString());
        // Запомним что это удаление или добавление
        Integer idSaved = edizm.getId();
        try {
            edizmRepositoryJdbc.save(edizm);
        } catch (DataAccessException e) {
            if (idSaved == null) {
                // Это было добавление - обнулим id
                edizm.setId(null);
            }
            result.rejectValue("id", "", DatebaseUtils.makeErrorMessage(e));
            return "edizm/form";
        }
        return "redirect:/edizm/index";
    }


}
