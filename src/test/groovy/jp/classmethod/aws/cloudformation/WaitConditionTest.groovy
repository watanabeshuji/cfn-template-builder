package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class WaitConditionTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("WaitConditionTest_default.csv").getFile())
        def actual = WaitCondition.load(input)
        assert actual == [
                new WaitCondition(id: 'WebServerWaitCondition',   Name: 'WebServerWaitCondition'  ,Handle: 'WebServerWaitHandle'  ,Timeout: '1000', DependsOn: ['WebServer']),
                new WaitCondition(id: 'BatchServerWaitCondition', Name: 'BatchServerWaitCondition',Handle: 'BatchServerWaitHandle',Timeout: '900',  DependsOn: [])
        ]
    }

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

    @Test
    void "toResourceMap DependsOnなし"() {
        def sut = new WaitCondition(id: 'BatchServerWaitCondition', Name: 'BatchServerWaitCondition',Handle: 'BatchServerWaitHandle',Timeout: '900',  DependsOn: [])
        def expected = [
            "BatchServerWaitCondition": [
                'Type': 'AWS::CloudFormation::WaitCondition',
                'Properties': [
                    'Handle': ['Ref': 'BatchServerWaitHandle'],
                    'Timeout': '900'
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
