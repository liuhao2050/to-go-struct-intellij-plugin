package entry

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class JsonStructBuilderTest {

    @Test
    fun gen() {
        var builder = JsonStructBuilder("T")
        var result = builder.gen("[{}]")
        assertEquals(
            result,
            """type T struct {
}"""
        )
        builder = JsonStructBuilder("T")
        result = builder.gen("""{}""")
        assertEquals(
            result,
            """type T struct {
}"""
        )

        builder = JsonStructBuilder("T")
        builder.setConfig("json:\"%s\" bson:\"%s\"", false)
        result = builder.gen("""{"a": 1, "A": "b"}""")
        println(result)
        assertEquals(result, """type T struct {
    A    int    `json:"a" bson:"a"`
    A    string    `json:"A" bson:"A"`
}""")

    }
}