package biz.gelicon.dub_test_spring.model;

public class Towntype {

    public Integer id;
    public String name;
    public String code;

    public Towntype(
            Integer id,
            String name,
            String code
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return id.toString() + " " + name + " " + code;
    }

}
