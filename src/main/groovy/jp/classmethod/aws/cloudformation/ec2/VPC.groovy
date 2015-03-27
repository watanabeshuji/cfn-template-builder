package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::EC2::VPC
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-vpc.html
 */
@Canonical
class VPC extends Resource {

    String id
    def CidrBlock
    def EnableDnsSupport
    def EnableDnsHostnames

    def doValidate() {
        logicalId(this.id)
        require("CidrBlock", this.CidrBlock)
        bool("EnableDnsSupport", EnableDnsSupport)
        bool("EnableDnsHostnames", EnableDnsHostnames)
    }

    def toResourceMap() {
        doValidate()
        def map = [
            'Type': 'AWS::EC2::VPC',
            'Properties': [
                'CidrBlock': CidrBlock,
                'Tags': [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        if (EnableDnsSupport != null) map['Properties']['EnableDnsSupport'] = EnableDnsSupport
        if (EnableDnsHostnames != null) map['Properties']['EnableDnsHostnames'] = EnableDnsHostnames
        map
    }
}
