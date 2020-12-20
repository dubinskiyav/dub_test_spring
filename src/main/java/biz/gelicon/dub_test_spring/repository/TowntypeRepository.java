package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.model.Towntype;

import java.util.List;
import java.util.Optional;

public interface TowntypeRepository {

    int count();
    int insert(Towntype towntype);
    int update(Towntype towntype);
    int delete(Integer id);
    List<Towntype> findAll();
    List<Towntype> findByName(String name);
    Optional<Towntype> findById(Integer id);
    String getNameById(Integer id);
}
