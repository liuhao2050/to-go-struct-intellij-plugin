package entry;

public interface Builder {
    void setTagTemplate(String tpl);
    String gen(String input);
};
