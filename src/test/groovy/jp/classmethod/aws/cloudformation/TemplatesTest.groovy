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
    void "vpc_standard_multi_az.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc_standard_multi_az.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc_standard_multi_az")
        assert actual == expected
    }

    @Test
    void "vpc_standard_multi_az_public_only.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc_standard_multi_az_public_only.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc_standard_multi_az_public_only")
        assert actual == expected
    }

    @Test
    void "vpc_standard_single_az.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc_standard_single_az.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc_standard_single_az")
        assert actual == expected
    }

    @Test
    void "vpc_standard_single_az_pucblic_only.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc_standard_single_az_pucblic_only.groovy").getPath())
        def actual = toJsonObject(CloudFormation.load(input).toPrettyString())
        def expected = getExpected("vpc_standard_single_az_pucblic_only")
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
