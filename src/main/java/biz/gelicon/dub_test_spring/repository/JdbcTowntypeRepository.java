package biz.gelicon.dub_test_spring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class JdbcTowntypeRepository implements TowntypeRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int count() {
        Integer i = jdbcTemplate
                .queryForObject(""
                                + " SELECT COUNT(*) "
                                + " FROM   towntype ",
                        Integer.class);
        return Objects.requireNonNullElse(i, 0);
    }
}
