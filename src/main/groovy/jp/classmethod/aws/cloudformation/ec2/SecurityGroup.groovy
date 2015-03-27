package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::SecurityGroup
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group.html
 *
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class SecurityGroup {

    def id
    def VpcId
    def Description
    List SecurityGroupIngress = []

    def SecurityGroup() {
    }

    def toResourceMap() {
        [
            'Type'      : 'AWS::EC2::SecurityGroup',
            'Properties': [
                'VpcId'               : VpcId,
                'GroupDescription'    : Description,
                'SecurityGroupIngress': SecurityGroupIngress,
                'Tags'                : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }

}

