package entry;

import com.google.common.base.CaseFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class JsonStructBuilder implements Builder {

    private String title;

    public JsonStructBuilder(String title) {
        this.title = title;
    }

    @Override
    public void set_title(String title) {
        this.title = title;
    }


    @Override
    public String gen(String str) {
        JSONObject json = new JSONObject(str);
        return build(json, 0);
    }

    String makeField(String field, String Type, int deep) {
        String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, field);
        String str = "    " + name + "    " + Type + "    `json: \"" + field + "\"` \n";
        return makeTab(str, deep, false);
    }

    String makeTab(String t, int deep, boolean after) {
        String pre = "";
        for (int i = 0; i < deep; i++) {
            pre += "    ";
        }
        if (after) {
            return t + pre;
        }
        return pre + t;
    }

    private String type_assert(String s, Object v) {
        if (v instanceof Integer) {
            return "int";
        } else if (v instanceof Long) {
            return "int64";
        } else if (v instanceof Boolean) {
            return "bool";
        } else if (v instanceof Float || v instanceof Double) {
            return "float64";
        } else if (JSONObject.NULL.equals(v)) {
            return "interface{}";
        } else
            return "";
    }

    String build(JSONObject json, int deep) {
        Iterator iter = json.keys();
        Iterator it = iter;
        String s = "";
        if (deep == 0) {
            s = makeTab("type " + this.title + " struct {\n", deep, false);
        } else {
            s = s + "struct {\n";
        }

        while (it.hasNext()) {
            String k = (String) it.next();
            try {
                Object v = json.get(k);
                String type = type_assert(s, v);
                if (type.length() != 0) {
                    s += makeField(k, type, deep);
                }

                if (v instanceof JSONObject) {
                    JSONObject item = (JSONObject) v;
                    s += makeField(k, build(item, deep + 1), deep);
                } else if (v instanceof JSONArray) {
                    JSONArray values = json.getJSONArray(k);

                    // []
                    if (values.length() == 0) {
                        s += makeField(k, "[]interface{}", deep);
                        continue;
                    }

                    // [1, 2] or ["1", "2"] ...
                    Object obj = values.get(0);
                    type = type_assert(s, obj);
                    if (type.length() != 0) {
                        s += makeField(k, "[]" + type, deep);
                        continue;
                    }

                    //[{"a": ...}]
                    System.out.println(obj.getClass().getSimpleName());
                    if (obj instanceof JSONObject) {
                        JSONObject item = values.getJSONObject(0);
                        s += makeField(k, "[]" + build(item, deep + 1), deep);
                    }


                }
            } catch (JSONException e2) {
                e2.printStackTrace();
                throw e2;
            }
        }
        s = makeTab(s, deep, true) + "}";
        return s;
    }


}
