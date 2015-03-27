package jp.classmethod.aws.cloudformation.ec2

import org.junit.Test

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class VPCGatewayAttachmentTest {

    @Test
    void "byids_toResourceMap"() {
        def sut = new VPCGatewayAttachment(id: 'InternetGatewayAttachment', VpcId: [Ref: 'VPC'], InternetGatewayId: [Ref: 'InternetGateway'])
        def expected = [
            'Type': 'AWS::EC2::VPCGatewayAttachment',
            'Properties': [
                'VpcId': [Ref: 'VPC'],
                'InternetGatewayId': [Ref: 'InternetGateway']
            ]
        ]
        assert sut.toResourceMap() == expected
    }
}
