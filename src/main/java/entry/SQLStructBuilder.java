package entry;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.util.lang.Consumer;
import com.android.aapt.Resources;
import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SQLStructBuilder implements Builder, Consumer<SQLColumnDefinition> {

    List<SQLColumnDefinition> cols = new ArrayList<SQLColumnDefinition>() {
    };

    HashMap<String, String> type_map = new HashMap<String, String>();

    public SQLStructBuilder() {
        type_map.put("INT", "int");
        type_map.put("LONG", "int64");
        type_map.put("TINYINT", "int");
        type_map.put("SMALLINT", "int");
        type_map.put("MEDIUMINT", "int64");
        type_map.put("INTEGER", "int");
        type_map.put("BIGINT", "int64");
        type_map.put("FLOAT", "float64");
        type_map.put("DOUBLE", "float64");
        type_map.put("DECIMAL", "float64");

        type_map.put("DATE", "time.Time");
        type_map.put("TIME", "time.Time");
        type_map.put("YEAR", "string");
        type_map.put("DATETIME", "time.Time");
        type_map.put("TIMESTAMP", "time.Time");

        type_map.put("CHAR", "string");

        type_map.put("VARCHAR", "string");
        type_map.put("TINYBLOB", "string");
        type_map.put("TINYTEXT", "string");
        type_map.put("BLOB", "string");
        type_map.put("TEXT", "string");
        type_map.put("MEDIUMBLOB", "string");
        type_map.put("MEDIUMTEXT", "string");
        type_map.put("LONGBLOB", "string");
        type_map.put("LONGTEXT", "string");

    }


    @Override
    public void set_title(String title) {

    }

    String fmt_name(String name) {
        name = name.replaceAll("`", "").replaceAll("'", "").
                replaceAll("\"", "");
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name).
                replaceAll("id", "ID").replaceAll("Id", "ID");
    }

    String convert_type(String name) {
        return type_map.getOrDefault(name.toUpperCase(), "unknown");
    }

    String makeField(String name, String type) {
        return "\t" + fmt_name(name) + "\t" + type + "`gorm:\"column\":" + name + "\"`\n";
    }

    @Override
    public String gen(String sql) {
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLCreateTableParser createTableParser = parser.getSQLCreateTableParser();
        SQLCreateTableStatement statement = createTableParser.parseCreateTable();


        statement.forEachColumn(this);


        StringBuilder sb = new StringBuilder();

        String tableName = fmt_name(statement.getName().getSimpleName());
        sb.append("type " + tableName + " {\n");
        for (SQLColumnDefinition i : cols) {
            String t = convert_type(i.getDataType().getName());
            String field = i.getNameAsString();
            sb.append(makeField(field, t));
        }
        sb.append("}");
        return sb.toString();
    }

    public void accept(SQLColumnDefinition t) {
        if (t.getNameAsString().toUpperCase().contains("KEY")) {
            return;
        }
        cols.add(t);
    }


}
