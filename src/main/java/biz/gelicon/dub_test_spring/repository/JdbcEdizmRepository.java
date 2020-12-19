package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.Edizm;
import biz.gelicon.dub_test_spring.EdizmController;
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
public class JdbcEdizmRepository implements EdizmRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcEdizmRepository.class);

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
    public int save(Edizm edizm) {
        logger.info("Saving...{}", edizm.toString());
        return jdbcTemplate.update(""
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
    }

    @Override
    public int update(Edizm edizm) {
        if (true) {
        return namedParameterJdbcTemplate.update(""
                        + " UPDATE edizm SET "
                        + "   edizm_name = :edizm_name, "
                        + "   edizm_notation = :edizm_notation, "
                        + "   edizm_blockflag = :edizm_blockflag , "
                        + "   edizm_code = :edizm_code "
                        + " WHERE edizm_id = :edizm_id )",
                new BeanPropertySqlParameterSource(edizm));
        } else {
            return jdbcTemplate.update(""
                            + " UPDATE edizm SET "
                            + "   edizm_name = ?, "
                            + "   edizm_notation = ?, "
                            + "   edizm_blockflag = ?, "
                            + "   edizm_code = ?"
                            + " WHERE edizm_id = ?)",
                    edizm.name,
                    edizm.notation,
                    edizm.blockflag,
                    edizm.code,
                    edizm.id
            );
        }
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update(""
                        + " DELETE FROM edizm "
                        + " WHERE edizm_id = ?)",
                id
        );
    }

    @Override
    public List<Edizm> findAll() {
        return jdbcTemplate.query(""
                        + " SELECT edizm_id, "
                        + "        edizm_name, "
                        + "        edizm_notation, "
                        + "        edizm_blockflag, "
                        + "        edizm_code "
                        + " FROM   edizm ",
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
        if (true) {
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
        } else {
            return jdbcTemplate.query(""
                            + " SELECT edizm_id "
                            + "        edizm_name, "
                            + "        edizm_notation, "
                            + "        edizm_blockflag, "
                            + "        edizm_code "
                            + " FROM   edizm "
                            + " WHERE  edizm_name LIKE ? ",
                    new Object[]{"%" + name + "%"},
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
    }

    @Override
    public Optional<Edizm> findById(Integer id) {
        if (true) {
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
                            Optional.of(new Edizm(
                                    rs.getInt("edizm_id"),
                                    rs.getString("edizm_name"),
                                    rs.getString("edizm_notation"),
                                    rs.getInt("edizm_blockflag"),
                                    rs.getString("edizm_code")
                            ))
            );
        } else {
            String sql = ""
                    + " SELECT edizm_id "
                    + "        edizm_name, "
                    + "        edizm_notation, "
                    + "        edizm_blockflag, "
                    + "        edizm_code "
                    + " FROM   edizm "
                    + " WHERE  edizm_id = ? ";
            Edizm edizm = jdbcTemplate.queryForObject(sql, Edizm.class, id);
            if (edizm != null) {
                return Optional.of(edizm);
            }
            return Optional.empty();
        }
    }

    @Override
    public String getNameById(Integer id) {
        String sql = ""
                + " SELECT edizm_name "
                + " FROM   edizm "
                + " WHERE  edizm_id = ? ";
        return jdbcTemplate.queryForObject(sql,String.class,id);
    }
}
