package biz.gelicon.dub_test_spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller  /* Аннотация @Controller - говорит о том, что данный класс является контроллером.
                Контроллеры в Spring обрабатывают HTTP запросы на определенный адреса. */
public class EdizmController {

    @RequestMapping(value = "/edizm")  /* Данная аннотация говорит нам о том, что данный метод
                                          обрабатывает HTTP GET запросы на адрес /edizm.
                                          Иными словами, данный метод сработает, если кто-то
                                          перейдет по адресу /edizm. */
    public String edizmController(
            @RequestParam(name = "driver", required = false,  defaultValue = "postgresql") String driverName,
                /* Аннотация @RequestParam говорит о том, что параметр String driverName - является параметром url
                   имеет имя driver, не обязательный, если не указан - postgresql
                   вызывается так http://localhost:8080/edizm?driver=postgresql */
            @RequestParam(name = "host", required = false,  defaultValue = "10.15.3.39") String hostName,
            @RequestParam(name = "port", required = false,  defaultValue = "5432") String portName,
            @RequestParam(name = "db", required = false,  defaultValue = "PS_DEVELOP_TRUNK") String dbName,
            @RequestParam(name = "user", required = false,  defaultValue = "SYSDBA") String userName,
            @RequestParam(name = "password", required = false,  defaultValue = "masterkey") String passWord,
            Model model  /* пары ключ-значение
                            С помощью данного параметра мы можем передавать данные из Java кода в html страницы */
    ) {
        model.addAttribute("host", hostName); /* Передаем в html имя хоста, полученное из html запроса
                                                 в html извлекать так (на примере тега p):
                                                 <p th:text="'Имя хоста: ' + ${host}" />
                                                 Атрибут th у тега p - это инструмент движка шаблонов Thymeleaf
                                              */
        model.addAttribute("host", hostName);
        model.addAttribute("port", portName);
        model.addAttribute("db", dbName);
        model.addAttribute("user", userName);
        model.addAttribute("password", passWord);
        return "edizm_null"; /* метод контроллера должен вернуть имя представления.
                           Далее Spring будет искать html файл, с таким именем,
                           который и вернет в качестве ответа на HTTP запрос.
                           В данном случае возьмет edizm_null.html а вернет как edizm.html */
    }
}
