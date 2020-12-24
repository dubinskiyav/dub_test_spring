package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.model.Edizm;
import biz.gelicon.dub_test_spring.utils.DatebaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
public class EdizmRepositoryJdbc implements EdizmRepository {

    private static final Logger logger = LoggerFactory.getLogger(EdizmRepositoryJdbc.class);

    // Spring Boot создаст и отконфигурирует DataSource и JdbcTemplate
    // для этого добавить @Autowired
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int count() {
        Integer i = jdbcTemplate
                .queryForObject(""
                                + " SELECT COUNT(*) "
                                + " FROM   edizm ",
                        Integer.class);
        return Objects.requireNonNullElse(i, 0);
    }

    @Override
    public int insert(Edizm edizm) {
        logger.info("Saving...{}", edizm.toString());
        // Как то надо получить значение edizm_id
        DatebaseUtils datebaseUtils = new DatebaseUtils();
        edizm.id = datebaseUtils.getSequenceNextValue("edizm_id_gen", jdbcTemplate);
        Integer result = -1;
        result = jdbcTemplate.update(""
                        + " INSERT INTO edizm ("
                        + "   edizm_id, "
                        + "   edizm_name, "
                        + "   edizm_notation, "
                        + "   edizm_blockflag, "
                        + "   edizm_code) VALUES(?,?,?,?,?)",
                edizm.id,
                edizm.name,
                edizm.notation,
                edizm.blockflag,
                edizm.code
        );
        return result;
    }

    @Override
    public int update(Edizm edizm) {
        //Названия параметров должны совпадать с полями
        // и обязательно должны быть геттеры на все поля
        return namedParameterJdbcTemplate.update(""
                        + " UPDATE edizm SET "
                        + "   edizm_name = :name, "
                        + "   edizm_notation = :notation, "
                        + "   edizm_blockflag = :blockflag , "
                        + "   edizm_code = :code "
                        + " WHERE edizm_id = :id ",
                new BeanPropertySqlParameterSource(edizm));
    }

    public int save(Edizm edizm) {
        if (edizm.id == null) {
            return insert(edizm);
        } else {
            return update(edizm);
        }
    }

    @Override
    public int delete(Integer id) {
        Integer i = jdbcTemplate.update(""
                        + " DELETE FROM edizm "
                        + " WHERE edizm_id = ? ",
                id
        );
        return i;
    }

    @Override
    public List<Edizm> findAll() {
        return jdbcTemplate.query(""
                        + " SELECT edizm_id, "
                        + "        edizm_name, "
                        + "        edizm_notation, "
                        + "        edizm_blockflag, "
                        + "        edizm_code "
                        + " FROM   edizm "
                        + " ORDER BY edizm_name ",
                (rs, rowNum) ->
                        new Edizm(
                                rs.getInt("edizm_id"),
                                rs.getString("edizm_name"),
                                rs.getString("edizm_notation"),
                                rs.getInt("edizm_blockflag"),
                                rs.getString("edizm_code")
                        )
        );
    }

    @Override
    public List<Edizm> findByName(String name) {
        String sql = ""
                + " SELECT edizm_id, "
                + "        edizm_name, "
                + "        edizm_notation, "
                + "        edizm_blockflag, "
                + "        edizm_code "
                + " FROM   edizm "
                + " WHERE  edizm_name LIKE :edizm_name ";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("edizm_name", "%" + name + "%");

        return namedParameterJdbcTemplate.query(sql,
                mapSqlParameterSource,
                (rs, rowNum) ->
                        new Edizm(
                                rs.getInt("edizm_id"),
                                rs.getString("edizm_name"),
                                rs.getString("edizm_notation"),
                                rs.getInt("edizm_blockflag"),
                                rs.getString("edizm_code")
                        )
        );
    }

    @Override
    public Edizm findById(Integer id) {
        String sql = ""
                + " SELECT edizm_id, "
                + "        edizm_name, "
                + "        edizm_notation, "
                + "        edizm_blockflag, "
                + "        edizm_code "
                + " FROM   edizm "
                + " WHERE  edizm_id = :edizm_id ";
        return namedParameterJdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("edizm_id", id),
                (rs, rowNum) ->
                        new Edizm(
                                rs.getInt("edizm_id"),
                                rs.getString("edizm_name"),
                                rs.getString("edizm_notation"),
                                rs.getInt("edizm_blockflag"),
                                rs.getString("edizm_code")
                        )
        );
    }

    @Override
    public String getNameById(Integer id) {
        // todo Сделать по человечески
        String sql = ""
                + " SELECT edizm_name "
                + " FROM   edizm "
                + " WHERE  edizm_id = ? ";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}