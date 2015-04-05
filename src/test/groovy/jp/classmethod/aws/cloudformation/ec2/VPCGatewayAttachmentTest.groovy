package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class VPCGatewayAttachmentTest {


    @Test
    void "load vpcGatewayAttachment.groovy"() {
        Path input = getPath("/templates/resources/vpcGatewayAttachment.groovy")
        def actual = VPCGatewayAttachment.load(input)
        assert actual == [
            new VPCGatewayAttachment(id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"])
        ]
    }

    @Test
    void "byids_toResourceMap"() {
        def sut = new VPCGatewayAttachment(id: 'InternetGatewayAttachment', VpcId: [Ref: 'VPC'], InternetGatewayId: [Ref: 'InternetGateway'])
        def expected = [
            'Type'      : 'AWS::EC2::VPCGatewayAttachment',
            'Properties': [
                'VpcId'            : [Ref: 'VPC'],
                'InternetGatewayId': [Ref: 'InternetGateway']
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = VPCGatewayAttachment.newInstance(
            id: 'InternetGatewayAttachment',
            VpcId: [Ref: 'VPC'],
            InternetGatewayId: [Ref: 'InternetGateway'])
        assert sut.refIds == ['VPC', 'InternetGateway']
    }

    @Test(expected = ValidErrorException)
    void "VpcId必須"() {
        VPCGatewayAttachment.newInstance(id: "InternetGatewayAttachment", InternetGatewayId: [Ref: 'InternetGateway'])
    }

    @Test(expected = ValidErrorException)
    void "InternetGateway必須"() {
        VPCGatewayAttachment.newInstance(id: "InternetGatewayAttachment", VpcId: [Ref: 'VPC'])
    }
}
