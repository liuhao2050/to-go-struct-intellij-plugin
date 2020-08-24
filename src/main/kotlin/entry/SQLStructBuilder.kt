package entry

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import com.alibaba.druid.util.lang.Consumer
import com.google.common.base.CaseFormat
import java.util.*

class SQLStructBuilder : Builder, Consumer<SQLColumnDefinition> {
    var cols: MutableList<SQLColumnDefinition> = object : ArrayList<SQLColumnDefinition?>() {}
    var type_map = HashMap<String, String>()
    override fun set_title(title: String) {}
    private fun fmt_name(name: String): String {
        var name = name
        name = clearName(name)
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name).replace("id".toRegex(), "ID").replace("Id".toRegex(), "ID")
    }

    private fun convert_type(name: String): String {
        return type_map.getOrDefault(name.toUpperCase(), "unknown")
    }

    private fun clearName(name: String): String {
        return name.replace("`".toRegex(), "").replace("'".toRegex(), "").replace("\"".toRegex(), "")
    }

    private fun makeField(name: String, type: String): String {
        var name = name
        name = clearName(name)
        return """	${fmt_name(name)}	$type `json:"$name" gorm:"column:$name"`
"""
    }

    override fun gen(sql: String): String {
        val parser = MySqlStatementParser(sql)
        val createTableParser = parser.sqlCreateTableParser
        val statement = createTableParser.parseCreateTable()
        statement.forEachColumn(this)
        val sb = StringBuilder()
        val tableName = fmt_name(statement.name.simpleName)
        sb.append("type $tableName struct {\n")
        for (i in cols) {
            val t = convert_type(i.dataType.name)
            val field = i.nameAsString
            sb.append(makeField(field, t))
        }
        sb.append("}\n\n")
        sb.append(table_receiver(tableName, statement.name.simpleName))
        return sb.toString()
    }

    private fun table_receiver(name: String, tableName: String): String {
        var tableName = tableName
        tableName = clearName(tableName)
        var s = "func (m *$name) TableName() string {\n"
        s += "\treturn \"$tableName\"\n}"
        return s
    }

    override fun accept(t: SQLColumnDefinition) {
        if (t.nameAsString.toUpperCase().contains("KEY")) {
            return
        }
        cols.add(t)
    }

    init {
        type_map["INT"] = "int"
        type_map["LONG"] = "int64"
        type_map["TINYINT"] = "int"
        type_map["SMALLINT"] = "int"
        type_map["MEDIUMINT"] = "int64"
        type_map["INTEGER"] = "int"
        type_map["BIGINT"] = "int64"
        type_map["FLOAT"] = "float64"
        type_map["DOUBLE"] = "float64"
        type_map["DECIMAL"] = "float64"
        type_map["DATE"] = "time.Time"
        type_map["TIME"] = "time.Time"
        type_map["YEAR"] = "string"
        type_map["DATETIME"] = "time.Time"
        type_map["TIMESTAMP"] = "time.Time"
        type_map["CHAR"] = "string"
        type_map["VARCHAR"] = "string"
        type_map["TINYBLOB"] = "string"
        type_map["TINYTEXT"] = "string"
        type_map["BLOB"] = "string"
        type_map["TEXT"] = "string"
        type_map["MEDIUMBLOB"] = "string"
        type_map["MEDIUMTEXT"] = "string"
        type_map["LONGBLOB"] = "string"
        type_map["LONGTEXT"] = "string"
    }
}