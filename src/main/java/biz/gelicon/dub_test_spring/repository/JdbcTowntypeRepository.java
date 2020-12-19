package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.Edizm;
import biz.gelicon.dub_test_spring.Towntype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTowntypeRepository implements TowntypeRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTowntypeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int count() {
        Integer i = jdbcTemplate
                .queryForObject(""
                                + " SELECT COUNT(*) "
                                + " FROM   towntype ",
                        Integer.class);
        return Objects.requireNonNullElse(i, 0);
    }

    @Override
    public int insert(Towntype towntype) {
        logger.info("Saving...{}", towntype.toString());
        return jdbcTemplate.update(""
                        + " INSERT INTO towntype ("
                        + "   towntype_id, "
                        + "   towntype_name, "
                        + "   towntype_code "
                        + " ) VALUES(?,?,?)",
                towntype.id,
                towntype.name,
                towntype.code
        );
    }

    @Override
    public int update(Towntype towntype) {
        return namedParameterJdbcTemplate.update(""
                        + " UPDATE towntype SET "
                        + "   towntype_name = :name, "
                        + "   towntype_code = :code "
                        + " WHERE towntype_id = :id ",
                new BeanPropertySqlParameterSource(towntype));
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update(""
                        + " DELETE FROM towntype "
                        + " WHERE towntype_id = ? ",
                id
        );
    }

    @Override
    public List<Towntype> findAll() {
        return jdbcTemplate.query(""
                        + " SELECT towntype_id, "
                        + "        towntype_name, "
                        + "        towntype_code "
                        + " FROM   towntype ",
                (rs, rowNum) ->
                        new Towntype(
                                rs.getInt("towntype_id"),
                                rs.getString("towntype_name"),
                                rs.getString("towntype_code")
                        )
        );
    }

    @Override
    public List<Towntype> findByName(String name) {
        String sql = ""
                + " SELECT towntype_id, "
                + "        towntype_name, "
                + "        towntype_code "
                + " FROM   towntype "
                + " WHERE  towntype_name LIKE :towntype_name ";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("towntype_name", "%" + name + "%");

        return namedParameterJdbcTemplate.query(sql,
                mapSqlParameterSource,
                (rs, rowNum) ->
                        new Towntype(
                                rs.getInt("towntype_id"),
                                rs.getString("towntype_name"),
                                rs.getString("towntype_code")
                        )
        );
    }

    @Override
    public Optional<Towntype> findById(Integer id) {
        String sql = ""
                + " SELECT towntype_id, "
                + "        towntype_name, "
                + "        towntype_code "
                + " FROM   towntype "
                + " WHERE  towntype_id = :towntype_id ";
        return namedParameterJdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("towntype_id", id),
                (rs, rowNum) ->
                        Optional.of(new Towntype(
                                rs.getInt("towntype_id"),
                                rs.getString("towntype_name"),
                                rs.getString("towntype_code")
                        ))
        );
    }

    @Override
    public String getNameById(Integer id) {
        // todo Сделать по человечески
        String sql = ""
                + " SELECT towntype_name "
                + " FROM   towntype "
                + " WHERE  towntype_id = ? ";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}
