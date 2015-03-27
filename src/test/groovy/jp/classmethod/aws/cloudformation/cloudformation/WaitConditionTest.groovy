package jp.classmethod.aws.cloudformation.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionTest {

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
