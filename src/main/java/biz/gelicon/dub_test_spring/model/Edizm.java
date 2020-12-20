package biz.gelicon.dub_test_spring.model;

// Обязательно public иначе в шаблоне не увидит!!!! и поля тоже public !!!!
public class Edizm {

    public Integer id;
    public String name;
    public String notation;
    public Integer blockflag;
    public String code;

    public Edizm(
            Integer id,
            String name,
            String notation,
            Integer blockflag,
            String code
    ) {
        this.id = id;
        this.name = name;
        this.notation = notation;
        this.blockflag = blockflag;
        this.code = code;
    }

    public Edizm() {
        // Значения по умолчанию
        this.blockflag = 0;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNotation() {
        return notation;
    }

    public Integer getBlockflag() {
        return blockflag;
    }

    public String getCode() {
        return code;
    }

    public Boolean getBlockflagB() {
        return blockflag != null && blockflag != 0;
    }

    public void setBlockflagB(Boolean b) {blockflag = b ? 1 : 0;}

    @Override
    public String toString() {
        return id.toString() + " " + name + " " + notation + " " + blockflag.toString() + " "
                + code;
    }

}
