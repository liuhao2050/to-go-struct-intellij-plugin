package entry

import com.alibaba.druid.sql.ast.SQLDataTypeImpl
import com.alibaba.druid.sql.ast.SQLExpr
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser
import com.alibaba.druid.util.lang.Consumer
import java.util.*

class SQLStructBuilder : Builder, Consumer<SQLColumnDefinition> {
    var cols = ArrayList<SQLColumnDefinition>()
    private var tpl = ""
    private var withCRUDs = true

    override fun setConfig(tpl: String, withCRUDs: Boolean) {
        this.tpl = tpl
        this.withCRUDs = withCRUDs
    }

    private fun String.convertType() = typeMap.getOrDefault(this.toLowerCase(), "unknown")

    private fun makeComment(comment: SQLExpr?): String {
        if (comment == null) {
            return ""
        }
        val text = comment.toString().clearName().replace("\n", " ")
        return " // $text"
    }

    private fun makeField(name: String, type: String, comment: SQLExpr?): String {
        val originName = name.clearName()
        val values = mapOf<String,String>("comment" to comment.toString().clearName().replace("\n", " "))
        val tag = replaceVarInTag(originName.makeTags(this.tpl), values)
        val commentText = makeComment(comment)
        return "\t${originName.fmtName()}\t$type$tag${commentText}\n"
    }

    private fun replaceVarInTag(tag:String, kv: Map<String,String>): String {
        for(k in kv) {
            tag.replace("$${k.key}", k.value)
        }
        return tag
    }

    override fun gen(sql: String): String? {
        cols = ArrayList()
        val parser = MySqlStatementParser(sql)
        val createTableParser = parser.sqlCreateTableParser
        val statement = createTableParser.parseCreateTable()
        statement.forEachColumn(this)
        val sb = StringBuilder()
        val modelName = statement.name.simpleName.fmtName()
        sb.append("type $modelName struct {\n")
        for (i in cols) {
            val tpe = i.dataType
            var name = tpe.name
            if (tpe is SQLDataTypeImpl) {
                if (tpe.isUnsigned) {
                    name += " unsigned"
                }
            }
            val t = name.convertType()
            val field = i.nameAsString

            sb.append(makeField(field, t, i.comment))
        }
        sb.append("}\n\n")
        sb.append(tableReceiver(modelName, statement.name.simpleName))
        if (withCRUDs) {
            sb.append("\n\n").append(modelName.makeDaoFunc())
            sb.append("\n\n").append(modelName.makeCreateFunc())
            sb.append("\n\n").append(modelName.makeGetFunc())
            sb.append("\n\n").append(modelName.makeListFunc())
            sb.append("\n\n").append(modelName.makeUpdateFunc())
            sb.append("\n\n").append(modelName.makeDeleteFunc())
        }
        return sb.toString()
    }

    private fun tableReceiver(name: String, tableName: String): String {
        val modelName = tableName.clearName()
        var s = "func (m *$name) TableName() string {\n"
        s += "\treturn \"$modelName\"\n}"
        return s
    }


    override fun accept(t: SQLColumnDefinition) {
        // The `KEY` field should be skipped.
        // this may cause `key` field missing, but we have no better way found QAQ
        if (t.nameAsString.toUpperCase() == "KEY") {
            return
        }
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
        "varbinary" to "[]byte",
        "boolean" to "bool"
    )

}
