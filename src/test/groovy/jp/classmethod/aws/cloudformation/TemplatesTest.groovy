package jp.classmethod.aws.cloudformation

import groovy.json.JsonSlurper
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class TemplatesTest {

    @Test
    void "empty.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/empty.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("empty")
        assert actual == expected
    }

    @Test
    void "vpc.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc")
        assert actual == expected
    }

    @Test
    void "vpc_standard_simple.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc_standard_simple.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc_standard_simple")
        assert actual == expected
    }

    private toJsonObject(String jsonText) {
        JsonSlurper slurper = new JsonSlurper()
        slurper.parseText(jsonText)
    }

    private getExpected(String name) {
        JsonSlurper slurper = new JsonSlurper()
        slurper.parse(getClass().getResource("/templates/${name}.expected"))
    }
}
