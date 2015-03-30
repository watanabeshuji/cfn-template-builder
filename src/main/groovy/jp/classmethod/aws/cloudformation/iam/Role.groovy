package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Role extends Resource {

    def id
    final def Type = 'AWS::IAM::Role'
    def Path = '/'
    def AssumeRolePolicyDocument = [
        Version  : '2012-10-17',
        Statement: [
            [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
        ]
    ]

    def Role() {
    }

    def toResourceMap() {
        [
            'Type'      : Type,
            'Properties': [
                'AssumeRolePolicyDocument': AssumeRolePolicyDocument,
                'Path'                    : Path
            ]
        ]
    }


}
