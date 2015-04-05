package jp.classmethod.aws.cloudformation.cloudformation

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionHandleTest {


    @Test
    void "load waitConditionHandle.groovy"() {
        Path input = getPath("/templates/resources/waitConditionHandle.groovy")
        def actual = WaitConditionHandle.load(input)
        def expected = [
            new WaitConditionHandle(id: 'WebServerWaitConditionHandle')
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap"() {
        def sut = new WaitConditionHandle(id: 'WebServerWaitHandle')
        def expected = [
            'Type': 'AWS::CloudFormation::WaitConditionHandle'
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = WaitConditionHandle.newInstance(id: 'WaitConditionHandle')
        assert sut.refIds == []
    }

    @Test(expected = ValidErrorException)
    void "id 必須"() {
        WaitConditionHandle.newInstance([:])
    }

}
