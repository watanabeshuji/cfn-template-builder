package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.EIP
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class EIPTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("EIPTest_default.csv").getFile())
        def actual = EIP.load(input)
        assert actual == [
            new EIP(id: 'Web1EIP', Name: 'Web1EIP'),
            new EIP(id: 'Web2EIP', Name: 'Web2EIP')
        ]
    }

    @Test
    void "Instanceに紐尽くdefault.csvのload"() {
        File input = new File(getClass().getResource("EIPTest_withInstance.csv").getFile())
        def actual = EIP.load(input)
        assert actual == [
                new EIP(id: 'WebEIP', Name: 'WebEIP', InstanceId: 'Web'),
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new EIP(id: 'Web1EIP', Name: 'web1-eip')
        def expected = [
            "Web1EIP": [
                'Type': 'AWS::EC2::EIP',
                'Properties': [
                    'Domain': 'vpc'
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "Instanceに紐尽くtoResourceMap"() {
        def sut = new EIP(id: 'WebEIP', Name: 'WebEIP', InstanceId: 'Web')
        def expected = [
            "WebEIP": [
                'Type': 'AWS::EC2::EIP',
                'Properties': [
                    'Domain': 'vpc',
                    InstanceId: [Ref: 'Web']
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
