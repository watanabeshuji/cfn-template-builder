package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.DSLSupport
import jp.classmethod.aws.cloudformation.Resource

import java.nio.file.Path

/**
 * AWS::EC2::VPCGatewayAttachment
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc-gateway-attachment.html
 *
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class VPCGatewayAttachment extends Resource {

    final def Type = 'AWS::EC2::VPCGatewayAttachment'
    def id
    def VpcId
    def InternetGatewayId

    def VPCGatewayAttachment() {
    }

    def toResourceMap() {
        [
            'Type'      : Type,
            'Properties': [
                'VpcId'            : this.VpcId,
                'InternetGatewayId': InternetGatewayId
            ]
        ]
    }

}
