package biz.gelicon.dub_test_spring.repository;

import biz.gelicon.dub_test_spring.model.Edizm;

import java.util.List;

// Мы должны унаследовать свой интерфейс от JpaRepository,
// иначе Spring Data не предоставит реализацию для нашего репозитория (обязательно).
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
