package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::VPCGatewayAttachment
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc-gateway-attachment.html
 *
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class VPCGatewayAttachment {

    def id
    def VpcId
    def InternetGatewayId

    def VPCGatewayAttachment() {
    }

    def toResourceMap() {
        [
            'Type': 'AWS::EC2::VPCGatewayAttachment',
            'Properties': [
                'VpcId': this.VpcId,
                'InternetGatewayId': InternetGatewayId
            ]
        ]
    }
}
