package jp.classmethod.aws.cloudformation.cloudformation

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionTest {

    @Test
    void "load waitCondition.groovy"() {
        Path input = getPath("/templates/resources/waitCondition.groovy")
        def actual = WaitCondition.load(input)
        def expected = [
            new WaitCondition(id: 'WebServerWaitCondition', Handle: [Ref: "WebServerWaitHandle"], Timeout: "1000")
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap"() {
        def sut = new WaitCondition(id: 'WebServerWaitCondition', Handle: [Ref: 'WebServerWaitHandle'], Timeout: '1000')
        def expected = [
            'Type'      : 'AWS::CloudFormation::WaitCondition',
            'Properties': [
                'Handle' : ['Ref': 'WebServerWaitHandle'],
                'Timeout': '1000'
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
