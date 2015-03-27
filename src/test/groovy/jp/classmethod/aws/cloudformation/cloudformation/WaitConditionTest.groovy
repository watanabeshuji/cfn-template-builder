package jp.classmethod.aws.cloudformation.cloudformation

import jp.classmethod.aws.cloudformation.cloudformation.WaitCondition
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionTest {

    @Test
    void "toResourceMap"() {
        def sut = new WaitCondition(id: 'WebServerWaitCondition',   Name: 'WebServerWaitCondition'  ,Handle: 'WebServerWaitHandle'  ,Timeout: '1000', DependsOn: ['WebServer'])
        def expected = [
            "WebServerWaitCondition": [
                'Type': 'AWS::CloudFormation::WaitCondition',
                'DependsOn': ['WebServer'],
                'Properties': [
                    'Handle': ['Ref': 'WebServerWaitHandle'],
                    'Timeout': '1000'
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
