package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::SecurityGroup
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group.html
 *
 * Created by watanabeshuji on 2014/08/13.
 */
@Canonical
class SecurityGroup extends Resource {

    final def Type = 'AWS::EC2::SecurityGroup'
    def id
    def VpcId
    def Description
    List<SecurityGroupIngress> SecurityGroupIngress = []

    def SecurityGroup() {
    }

    def toResourceMap() {
        [
            'Type'      : Type,
            'Properties': [
                'VpcId'               : VpcId,
                'GroupDescription'    : Description,
                'SecurityGroupIngress': SecurityGroupIngress.collect { it.toInlineMap() },
                'Tags'                : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
    }

}

