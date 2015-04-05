package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class EIPTest {

    @Test
    void "load eip.groovy"() {
        Path input = getPath("/templates/resources/eip.groovy")
        def actual = EIP.load(input)
        assert actual == [
            new EIP(id: 'PublicIP'),
            new EIP(id: "WebIP", InstanceId: [Ref: "Web"])
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new EIP(id: 'Web1EIP')
        def expected = [
            'Type'      : 'AWS::EC2::EIP',
            'Properties': [
                'Domain': 'vpc'
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "Instanceに紐尽くtoResourceMap"() {
        def sut = new EIP(id: 'WebEIP', InstanceId: [Ref: 'Web'])
        def expected = [
            'Type'      : 'AWS::EC2::EIP',
            'Properties': [
                'Domain'  : 'vpc',
                InstanceId: [Ref: 'Web']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = EIP.newInstance(id: 'WebEIP', InstanceId: [Ref: 'Web'])
        assert sut.refIds == ['Web']
    }


    @Test(expected = ValidErrorException)
    void "id 必須"() {
        EIP.newInstance(InstanceId: [Ref: 'Web'])
    }
}
