package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Role {

    def id
    def Path = '/'
    def AssumeRolePolicyDocument = [
        Version: '2012-10-17',
        Statement: [
            [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
        ]
    ]

    def Role() {
    }

    def toResourceMap() {
        [
            'Type': 'AWS::IAM::Role',
            'Properties': [
                'AssumeRolePolicyDocument': AssumeRolePolicyDocument,
                'Path': Path
            ]
        ]
    }


}