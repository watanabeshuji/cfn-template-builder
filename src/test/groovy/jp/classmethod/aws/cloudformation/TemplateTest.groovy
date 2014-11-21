package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/17.
 */
class TemplateTest {

    @Test
    void "build"() {
        String dirName = new File("./cfn").absolutePath
        def expected = new File(dirName, "cfn.template").text
        def actual = Template.build(dirName).toPrettyString()
        println actual
        assertText(actual, expected)
    }


    @Test
    void "build_templates_minimum"() {
        String dirName = new File("./src/main/resources/templates/minimum").absolutePath
        def expected = new File(dirName, "cfn.template").text
        def actual = Template.build(dirName).toPrettyString()
        println actual
        assertText(actual, expected)
    }


    void assertText(actual, expected) {
        if (actual == expected) return
        def actualLines = actual.split(/\n/)
        def expectedLines = expected.split(/\n/)
        int lines = Math.max(actualLines.size(), expectedLines.size())
        for (int l = 0; l < lines; l++) {
            assert actualLines[l] == expectedLines[l]
        }

    }

}
