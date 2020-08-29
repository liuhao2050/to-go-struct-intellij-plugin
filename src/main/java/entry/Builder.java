package entry;

public interface Builder {
    void setConfig(String tpl, boolean withCRUDs);
    String gen(String input);
};
