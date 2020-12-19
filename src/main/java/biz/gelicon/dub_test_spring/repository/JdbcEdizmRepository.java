package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.Edizm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcEdizmRepository implements EdizmRepository {

    // Spring Boot создаст и отконфигурирует DataSource и JdbcTemplate
    // для этого добавить @Autowired
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                        + " SELECT edizm_id "
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

    @Override
    public Optional<Edizm> findById(Integer id) {
        String sql = ""
                + " SELECT edizm_id "
                + "        edizm_name, "
                + "        edizm_notation, "
                + "        edizm_blockflag, "
                + "        edizm_code "
                + " FROM   edizm "
                + " WHERE  edizm_id = ? ";
        Edizm edizm = jdbcTemplate.queryForObject(sql,Edizm.class, id);
        if (edizm != null) {
            return Optional.of(edizm);
        }
        return Optional.empty();
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
