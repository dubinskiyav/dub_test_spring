package biz.gelicon.dub_test_spring.utils;


import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ThreadLocalRandom;

// Общие методы работы с базой данных
public class DatebaseUtils {

    public static String dbType;

//    @Autowired
//    private ApplicationContext context;

    // Возвращает следующее значение генератора
    public Integer getSequenceNextValue(
            String sequenceName,
            JdbcTemplate jdbcTemplate
    ) {
        if (sequenceName == null || jdbcTemplate == null || !isPostgreSQL()) {
            // integer	4 байта	типичный выбор для целых чисел	-2147483648 .. +2147483647
            return ThreadLocalRandom.current().nextInt(1000000000) + 1000000000;
        }
        return jdbcTemplate.queryForObject("SELECT nextval('" + sequenceName + "')", Integer.class);
    }

    // Устанавливает тип базы данных
    public static void setDbType() {
        // todo Необходима реализация полученя из application.properties
        String datasourceUrl = "jdbc:postgresql://10.15.3.39:5432/PS_DEVELOP_TRUNK";
        if (dbType != null) {return;} // Уже установлена
        if (datasourceUrl == null) {return;} // Не из чего устанавливать
        if (datasourceUrl.contains("postgresql")) {
            dbType = "postgresql";
            return;
        }
        // Не смогли определить - устанавливаем по умолчанию
        setDbTypeDefault();
    }

    // Проверяет тип базы данных
    // Если забыли установить до этого - устанавливает в по умолчанию
    public static String getDbType() {
        if (dbType == null) { // Еще не установлена
            // Установить
            setDbType();
        }
        // Вернуть
        return dbType;
    }

    // Тип базы данных по умолчанию - postgresql
    public static void setDbTypeDefault() {
        dbType = "postgresql";
    }

    // Проверяет, не PostgreSQL ли использутеся
    public Boolean isPostgreSQL() {
        return getDbType().contains("postgresql");
        // spring.datasource.url=jdbc:postgresql://10.15.3.39:5432/PS_DEVELOP_TRUNK
        //String url = context.getEnvironment().getProperty("spring.datasource.url");
        //if (url == null) {return false;}
        //return url.contains("postgresql");
    }
}
