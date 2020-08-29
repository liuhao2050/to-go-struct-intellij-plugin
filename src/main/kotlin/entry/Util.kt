package entry

fun String.underscoreToCamel(): String {
    val names = this.split("_")
    val sb = StringBuilder()
    for (n in names) {
        sb.append(n.firstToUpper())
    }
    return sb.toString()
}

fun String.fmtName(): String {
    var name = this.clearName().firstToUpper()
    if (name.contains("_")) name = name.underscoreToCamel()
    return name.replace("Id".toRegex(), "ID")
}

fun String.clearName() =
    this.replace("`".toRegex(), "").replace("'".toRegex(), "").replace("\"".toRegex(), "")


fun String.firstToUpper(): String {
    if (this.isEmpty()) return ""
    val ch = this.toCharArray()
    ch[0] = ch[0].toUpperCase()
    return String(ch)
}

// tpl should: `json:"%s" bson:"%s" etc...`
fun String.makeTags(tpl: String): String {
    if (tpl.isEmpty()) {
        return ""
    }
    return "    `" + tpl.replace("%s", this) + "`"
}

fun String.makeDaoFunc(): String {
    val dao = this + "Dao"
    return """
type $dao struct {
    sourceDB  *gorm.DB
    replicaDB []*gorm.DB
    m         *UserObject
}

func New$dao(ctx context.Context, dbs ...*gorm.DB) *$dao {
    dao := new($dao)
    switch len(dbs) {
    case 0:
        panic("database connection required")
    case 1:
        dao.sourceDB = dbs[0]
        dao.replicaDB = []*gorm.DB{dbs[0]}
    default:
        dao.sourceDB = dbs[0]
        dao.replicaDB = dbs[1:]
    }
    return dao
}
    """.trimIndent()
}

fun String.makeCreatefn(): String {
    val dao = this + "Dao"
    return """
func (d *$dao) Create(ctx context.Context, obj *$this) error {
	err := d.sourceDB.Model(d.m).Create(&obj).Error
	if err != nil {
		return fmt.Errorf("$dao: %w", err)
	}
	return nil
}"""
}