package entry;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonStructBuilderTest {
    JsonStructBuilder builder = new JsonStructBuilder("Gen");

    @Test
    public void set_title() {
    }

    @Test
    public void gen() {

        String json6 = "{\n" +
                "  \"aa\": 1,\n" +
                "\t\"bb\": [{\"c\": 1}],\n" +
                "\t\"cc\": {\"d\": 1},\n" +
                "\t\"dd\":[],\n" +
                "\t\"ee\": [1],\n" +
                "\t\"ff_hello\": [{\"f1\": {\"f2\": [1]}}]\n" +
                "}";
        String out = this.builder.gen(json6);
        System.out.println(out);


    }

    @Test
    public void makeField() {
    }

    @Test
    public void build() {
    }
}