package entry

import com.google.common.base.CaseFormat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonStructBuilder(private var title: String) : Builder {
    override fun setTitle(title: String) {
        this.title = title
    }

    override fun gen(str: String): String {
        val json = JSONObject(str)
        return build(json, 0)
    }

    private fun makeField(field: String, Type: String, deep: Int): String {
        var name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, field)
        name = name.replace("Id".toRegex(), "ID")
        val str = "    $name    $Type    `json:\"$field\"` \n"
        return makeTab(str, deep, false)
    }

    private fun makeTab(t: String, deep: Int, after: Boolean): String {
        var pre = ""
        for (i in 0 until deep) {
            pre += "    "
        }
        return if (after) {
            t + pre
        } else pre + t
    }

    private fun type_assert(s: String, v: Any): String {
        if (v is Int) {
            return "int"
        } else if (v is Long) {
            return "int64"
        } else if (v is Boolean) {
            return "bool"
        } else if (v is Float || v is Double) {
            return "float64"
        } else if (JSONObject.NULL == v) {
            return "interface{}"
        } else if (v is String) {
            return "string"
        }
        return ""
    }

    private fun build(json: JSONObject, deep: Int): String {
        val iter: Iterator<*> = json.keys()
        var s = ""
        s = if (deep == 0) {
            makeTab(
                """type ${title} struct {
""", deep, false
            )
        } else {
            """
     ${s}struct {
     
     """.trimIndent()
        }
        while (iter.hasNext()) {
            val k = iter.next() as String
            try {
                val v = json[k]
                var type = type_assert(s, v)
                if (type.length != 0) {
                    s += makeField(k, type, deep)
                }
                if (v is JSONObject) {
                    s += makeField(k, build(v, deep + 1), deep)
                } else if (v is JSONArray) {
                    val values = json.getJSONArray(k)

                    // []
                    if (values.length() == 0) {
                        s += makeField(k, "[]interface{}", deep)
                        continue
                    }

                    // [1, 2] or ["1", "2"] ...
                    val obj = values[0]
                    type = type_assert(s, obj)
                    if (type.length != 0) {
                        s += makeField(k, "[]$type", deep)
                        continue
                    }

                    //[{"a": ...}]
                    if (obj is JSONObject) {
                        val item = values.getJSONObject(0)
                        s += makeField(k, "[]" + build(item, deep + 1), deep)
                    }
                }
            } catch (e2: JSONException) {
                e2.printStackTrace()
                throw e2
            }
        }
        s = makeTab(s, deep, true) + "}"
        return s
    }

}