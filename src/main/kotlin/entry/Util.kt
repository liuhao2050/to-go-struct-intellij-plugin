package entry

fun String.underscoreToCamel(): String {
    val names = this.split("_")
    val sb = StringBuilder()
    for (n in names) {
        sb.append(n.firstToUpper())
    }
    return sb.toString()
}

//这里还是不把id改了吧，今天发现我的 VideoUrl 变成 VIDeoUrl,难受
fun String.fmtName(to: String): String {
    var name = this.clearName()
    if (name.contains("_")) name = name.underscoreToCamel()
    if (to == "json") name = name.firstToLower()
    else if (to == "go") name = name.firstToUpper()
    return name
}

fun String.clearName() =
    this.replace("`".toRegex(), "").replace("'".toRegex(), "").replace("\"".toRegex(), "")


fun String.firstToUpper(): String {
    if (this.isEmpty()) return ""
    val ch = this.toCharArray()
    ch[0] = ch[0].toUpperCase()
    return String(ch)
}

fun String.firstToLower(): String {
    if (this.isEmpty()) return ""
    val ch = this.toCharArray()
    ch[0] = ch[0].toLowerCase()
    return String(ch)
}