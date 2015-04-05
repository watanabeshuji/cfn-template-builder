package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId

/**
 * AWS::EC2::EIP
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class EIP extends Resource {

    static final def TYPE = 'AWS::EC2::EIP'
    static def DESC = '''\
AWS::EC2::EIP
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-eip.html

[Required Params]
- id

[Optional Params]
- InstanceId

[Sample: InternetGateway]
resources {
    eip id: "PublicIP"
    eip id: "WebIP", InstanceId: [Ref: "Web"]
}
'''
    def id
    def InstanceId

    static EIP newInstance(Map params) {
        convert(params)
        checkKeys(EIP, params, ['id', 'InstanceId'])
        logicalId(EIP, params)
        new EIP(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Domain': 'vpc'
            ]
        ]
        if (InstanceId) map['Properties']['InstanceId'] = InstanceId
        map
    }

}
