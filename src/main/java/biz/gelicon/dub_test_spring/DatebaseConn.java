package biz.gelicon.dub_test_spring;

import java.sql.Connection;

// Класс для работы с базой данных
public class DatebaseConn {


    String alias;
    String driverClassName;
    String url;
    String user;
    String passWord;

    public static Connection connection; // Текущий коннект
    public static DatebaseConn datebaseConn; // Текущая база данных

    DatebaseConn() {
    }

    DatebaseConn(
            String alias,
            String driverClassName,
            String url,
            String user,
            String passWord
    ) {
        this.alias = alias;
        this.driverClassName = driverClassName;
        this.url = url;
        this.user = user;
        this.passWord = passWord;
    }

}

