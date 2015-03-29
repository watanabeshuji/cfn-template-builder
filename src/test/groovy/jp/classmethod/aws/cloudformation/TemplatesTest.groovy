package jp.classmethod.aws.cloudformation

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
        def actual = CloudFormation.load(input).toPrettyString()
        def expected = getClass().getResource("/templates/empty.expected").text
        assert actual == expected
    }

    @Test
    void "vpc.groovy toPrettyString"() {
        Path input = Paths.get(getClass().getResource("/templates/vpc.groovy").getPath())
        def actual = CloudFormation.load(input).toPrettyString()
        def expected = getClass().getResource("/templates/vpc.expected").text
        assert actual == expected
    }

}
