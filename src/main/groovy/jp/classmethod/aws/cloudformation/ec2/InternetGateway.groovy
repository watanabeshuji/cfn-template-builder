package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::InternetGateway
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-internet-gateway.html
 */
@Canonical
class InternetGateway extends Resource {
    static def DESC = '''\
AWS::EC2::InternetGateway
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-internet-gateway.html

[Required Params]
- id

[Optional Params]
- Tags

[Sample: InternetGateway]
resources {
    internetGateway id: "InternetGateway"
}
'''

    final def Type = 'AWS::EC2::InternetGateway'
    def id
    def Tags = [:]

    def InternetGateway() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : Type,
            'Properties': [
                'Tags': []
            ]
        ]
        Tags.each {key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }
}
