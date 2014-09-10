package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/09/10.
 */
class UtilTest {

    @Test
    void "toCamelCase"() {
        assert Util.toCamelCase("hello-world") == "HelloWorld"
        assert Util.toCamelCase("helloWorld") == "HelloWorld"
        assert Util.toCamelCase("HelloWorld") == "HelloWorld"
    }

}
