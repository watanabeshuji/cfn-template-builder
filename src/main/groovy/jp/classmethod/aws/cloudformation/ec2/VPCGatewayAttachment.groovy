package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::VPCGatewayAttachment
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc-gateway-attachment.html
 *
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class VPCGatewayAttachment extends Resource {

    static final def TYPE = 'AWS::EC2::VPCGatewayAttachment'
    static def DESC = '''\
AWS::EC2::VPCGatewayAttachment
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc-gateway-attachment.html

[Required Params]
- id
- VpcId
- InternetGatewayId

[Optional Params]

[Sample: InternetGateway]
resources {
    vpcGatewayAttachment id: "InternetGatewayAttach", VpcId: [Ref: "VPC"], InternetGatewayId: [Ref: "InternetGateway"]
}
'''
    def id
    def VpcId
    def InternetGatewayId

    static VPCGatewayAttachment newInstance(Map params) {
        convert(params)
        checkKeys(VPCGatewayAttachment, params, ['id', 'VpcId', 'InternetGatewayId', 'Tags'])
        logicalId(VPCGatewayAttachment, params)
        require(VPCGatewayAttachment, ['VpcId', 'InternetGatewayId'], params)
        new VPCGatewayAttachment(params).withRefIds(params)
    }

    def toResourceMap() {
        [
            'Type'      : TYPE,
            'Properties': [
                'VpcId'            : this.VpcId,
                'InternetGatewayId': this.InternetGatewayId
            ]
        ]
    }

}
