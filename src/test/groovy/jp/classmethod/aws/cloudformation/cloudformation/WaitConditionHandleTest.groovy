package jp.classmethod.aws.cloudformation.cloudformation

import jp.classmethod.aws.cloudformation.cloudformation.WaitConditionHandle
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionHandleTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("WaitConditionHandleTest_default.csv").getFile())
        def actual = WaitConditionHandle.load(input)
        assert actual == [
            new WaitConditionHandle(id: 'WebServerWaitHandle',   Name: 'WebServerWaitHandle'),
            new WaitConditionHandle(id: 'BatchServerWaitHandle', Name: 'BatchServerWaitHandle')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new WaitConditionHandle(id: 'WebServerWaitHandle',   Name: 'WebServerWaitHandle')
        def expected = [
            "WebServerWaitHandle": [
                'Type': 'AWS::CloudFormation::WaitConditionHandle'
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
