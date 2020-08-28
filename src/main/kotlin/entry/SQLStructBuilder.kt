package entry

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import com.alibaba.druid.util.lang.Consumer
import java.util.*

class SQLStructBuilder : Builder, Consumer<SQLColumnDefinition> {
    var cols = ArrayList<SQLColumnDefinition>()
    private var sqlTagKey = "db"
    private val sqlTagValPrefix = ""

    override fun setTitle(title: String) {}

    private fun String.convertType() = typeMap.getOrDefault(this.toLowerCase(), "unknown")

    private fun makeField(name: String, type: String): String {
        val name1 = name.clearName()
        var sqlTagValPrefix = this.sqlTagValPrefix
        if (sqlTagValPrefix.trim().isNotEmpty()) sqlTagValPrefix += ":"
        return "\t${name1.fmtName("go")}\t$type `json:\"${name1.fmtName("json")}\" $sqlTagKey:\"$sqlTagValPrefix$name1\"`\n"
    }

    override fun gen(sql: String): String {
        val parser = MySqlStatementParser(sql)
        val createTableParser = parser.sqlCreateTableParser
        val statement = createTableParser.parseCreateTable()
        statement.forEachColumn(this)
        val sb = StringBuilder()
        val tableName = statement.name.simpleName.fmtName("go")
        sb.append("type $tableName struct {\n")
        for (i in cols) {
            val t = i.dataType.name.convertType()
            val field = i.nameAsString
            sb.append(makeField(field, t))
        }
        sb.append("}\n\n")
        sb.append(tableReceiver(tableName, statement.name.simpleName))
        return sb.toString()
    }

    private fun tableReceiver(name: String, tableName: String): String {
        val tableName1 = tableName.clearName()
        var s = "func (m *$name) TableName() string {\n"
        s += "\treturn \"$tableName1\"\n}"
        return s
    }


    override fun accept(t: SQLColumnDefinition) {
//        这个判断会跳过一些字段，比如 keyword 字段
//        if (t.nameAsString.toUpperCase().contains("KEY")) {
//            return
//        }
        cols.add(t)
    }

    private var typeMap = hashMapOf(
        "int" to "int",
        "integer" to "int",
        "tinyint" to "int8",
        "smallint" to "int16",
        "mediumint" to "int32",
        "bigint" to "int64",
        "int unsigned" to "uint",
        "integer unsigned" to "uint",
        "tinyint unsigned" to "uint8",
        "smallint unsigned" to "uint16",
        "mediumint unsigned" to "uint32",
        "bigint unsigned" to "uint64",
        "bit" to "byte",
        "bool" to "bool",
        "enum" to "string",
        "set" to "string",
        "varchar" to "string",
        "char" to "string",
        "tinytext" to "string",
        "mediumtext" to "string",
        "text" to "string",
        "longtext" to "string",
        "blob" to "[]byte",
        "tinyblob" to "[]byte",
        "mediumblob" to "[]byte",
        "longblob" to "[]byte",
        "date" to "time.Time",
        "datetime" to "time.Time",
        "timestamp" to "time.Time",
        "time" to "time.Time",
        "float" to "float32",
        "double" to "float64",
        "decimal" to "float64",
        "binary" to "[]byte",
        "varbinary" to "[]byte"
    )

}