package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.Edizm;

import java.util.List;
import java.util.Optional;

public interface EdizmRepository {
    int count();

    int insert(Edizm edizm);

    int update(Edizm edizm);

    int delete(Integer id);

    List<Edizm> findAll();

    List<Edizm> findByName(String name);

    Edizm findById(Integer id);

    String getNameById(Integer id);

}
