package jp.classmethod.aws.cloudformation.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionHandleTest {


    @Test
    void "toResourceMap"() {
        def sut = new WaitConditionHandle(id: 'WebServerWaitHandle')
        def expected = [
            'Type': 'AWS::CloudFormation::WaitConditionHandle'
        ]
        assert sut.toResourceMap() == expected
    }
}
