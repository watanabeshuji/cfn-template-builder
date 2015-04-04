package jp.classmethod.aws.cloudformation.ec2

import static jp.classmethod.aws.cloudformation.util.Params.convert
import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::VPC
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc.html
 */
@Canonical
class VPC extends Resource {

    static final def TYPE = 'AWS::EC2::VPC'
    static final def DESC = '''\
AWS::EC2::VPC
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc.html

[Required Params]
- id
- CidrBlock

[Optional Params]
- EnableDnsSupport
- EnableDnsHostnames
- Tags

[Sample: Simple VPC]
resources {
    vpc id: "VPC", CidrBlock: "10.0.0.0/16"
}

[Sample: Enable DNS Hostname and Name tag]
resources {
    vpc id: "VPC", CidrBlock: "192.168.0.0/16", EnableDnsSupport: true, EnableDnsHostnames: true, Tags: [Name: 'my-vpc']
}

'''

    String id
    def CidrBlock
    def EnableDnsSupport
    def EnableDnsHostnames
    def Tags = [:]

    static VPC newInstance(Map params) {
        convert(params)
        checkKeys(VPC, params, ['id', 'CidrBlock', 'EnableDnsSupport', 'EnableDnsHostnames', 'Tags'])
        logicalId(VPC, params)
        require(VPC, "CidrBlock", params)
        bool(VPC, ["EnableDnsSupport", "EnableDnsHostnames"], params)
        new VPC(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'CidrBlock': CidrBlock,
                'Tags'     : []
            ]
        ]
        if (EnableDnsSupport != null) map['Properties']['EnableDnsSupport'] = EnableDnsSupport
        if (EnableDnsHostnames != null) map['Properties']['EnableDnsHostnames'] = EnableDnsHostnames
        Tags.each {key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }
}
