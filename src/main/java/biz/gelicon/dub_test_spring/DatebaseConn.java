package biz.gelicon.dub_test_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Класс для работы с базой данных
public class DatebaseConn {

    private static final Logger logger = LoggerFactory.getLogger(DatebaseConn.class);

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

    // Проверяет коннект и если че переконнекчивает к текущему алиасу
    public Connection checkConnection(Connection c) {
        if (c != null) {
            logger.info("Checking connect...");
            // Проверим коннект
            try {
                // Примитивный запрос
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT dummy FROM   dual");
                rs.next();
                stmt.close();
                // все отлично
                logger.info("The connection is good, DATABASE NAME:"
                        + c.getMetaData().getDatabaseProductName());
                // возвращаем его же
                return c;
            } catch (SQLException throwables) {
                // Все плохо
                c = null;
                connection = null;
                logger.info("The connection lost", throwables);
            }
        }
        // Попробуем переконнектиться с текущим алиасом
        logger.info("Trying reconnect...");
        try {
            Class.forName(DatebaseConn.datebaseConn.driverClassName);
        } catch (ClassNotFoundException cnfe) {
            logger.error("Database provider class not found.", cnfe);
            return null;
        }
        try {
            c = DriverManager.getConnection(
                    datebaseConn.url,
                    datebaseConn.user,
                    datebaseConn.passWord
            );
            // Успешно
            connection = c;
            logger.info("The connection is good, DATABASE NAME:"
                    + c.getMetaData().getDatabaseProductName());
            return c;
        } catch (SQLException throwables) {
            // Не смогли
            logger.error("Database connection error.", throwables);
        }
        logger.error("The connection not found");
        return null;
    }

}

