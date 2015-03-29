package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::InternetGateway
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ec2-internet-gateway.html
 */
@Canonical
class InternetGateway extends Resource {

    def id

    def InternetGateway() {
    }

    def toResourceMap() {
        [
            'Type'      : 'AWS::EC2::InternetGateway',
            'Properties': [
                'Tags': [
                    ['Key': 'Name', 'Value': this.id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }
}
