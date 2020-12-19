package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.Edizm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

public class NamedParameterJdbcEdizmRepository extends JdbcEdizmRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int update(Edizm edizm) {
        return namedParameterJdbcTemplate.update(""
                        + " UPDATE edizm SET "
                        + "   edizm_name = :edizm_name, "
                        + "   edizm_notation = :edizm_notation, "
                        + "   edizm_blockflag = :edizm_blockflag , "
                        + "   edizm_code = :edizm_code "
                        + " WHERE edizm_id = :edizm_id )",
                new BeanPropertySqlParameterSource(edizm));
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
    }

    @Override
    public List<Edizm> findByName(String name) {

        String sql = ""
                + " SELECT edizm_id "
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

}
