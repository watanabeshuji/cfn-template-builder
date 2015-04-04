package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.DSLSupport
import jp.classmethod.aws.cloudformation.Resource

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId
import static jp.classmethod.aws.cloudformation.util.Valid.require

/**
 * AWS::EC2::VPCGatewayAttachment
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc-gateway-attachment.html
 *
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class VPCGatewayAttachment extends Resource {

    static final def TYPE = 'AWS::EC2::VPCGatewayAttachment'
    def id
    def VpcId
    def InternetGatewayId

    static VPCGatewayAttachment newInstance(Map params) {
        convert(params)
        checkKeys(TYPE, params, ['id', 'VpcId', 'InternetGatewayId', 'Tags'])
        logicalId(TYPE, params)
        require(TYPE, 'VpcId', params)
        require(TYPE, 'InternetGatewayId', params)
        def instance = new VPCGatewayAttachment(params)
        instance.addRefIds([params['VpcId'], params['InternetGatewayId']])
        instance
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
