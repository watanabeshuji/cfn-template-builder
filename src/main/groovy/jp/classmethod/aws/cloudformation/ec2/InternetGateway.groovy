package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId

/**
 * AWS::EC2::InternetGateway
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-internet-gateway.html
 */
@Canonical
class InternetGateway extends Resource {

    static final def TYPE = 'AWS::EC2::InternetGateway'
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

    def id
    def Tags = [:]

    static InternetGateway newInstance(Map params) {
        convert(params)
        checkKeys(InternetGateway, params, ['id', 'Tags'])
        logicalId(InternetGateway, params)
        new InternetGateway(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Tags': []
            ]
        ]
        Tags.each { key, value ->
            map['Properties']['Tags'] << ['Key': key, 'Value': value]
        }
        if (Tags['Name'] == null) map['Properties']['Tags'] << ['Key': 'Name', 'Value': id]
        if (Tags['Application'] == null) map['Properties']['Tags'] << ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
        map
    }
}
