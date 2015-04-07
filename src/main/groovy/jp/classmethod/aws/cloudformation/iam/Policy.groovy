package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::IAM::Policy
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-policy.html
 *
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Policy extends Resource {

    static final def TYPE = 'AWS::IAM::Policy'
    static def DESC = '''\
AWS::IAM::Policy
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-policy.html

[Required Params]
- id
- PolicyName
- PolicyDocument
- Roles or Groups or Users

[Optional Params]
none

[Sample]
def ec2PolicyDocument = [
    "Version"  : "2012-10-17",
    "Statement": [
        ["Effect": "Allow", "Action": "*", "Resource": "*"]
    ]
]
resources {
    policy id: "EC2Policy", PolicyName: "EC2", PolicyDocument: ec2PolicyDocument, Roles: ["RefEC2Role"]]
}

'''
    def id
    def PolicyName
    def PolicyDocument
    def Roles
    def Groups
    def Users

    static Policy newInstance(Map params) {
        convert(params)
        checkKeys(Policy, params, ['id', 'PolicyName', 'PolicyDocument', 'Roles', 'Groups', 'Users'])
        logicalId(Policy, params)
        require(Policy, ['PolicyName', 'PolicyDocument'], params)
        requireOneOf(Policy, ['Roles', 'Groups', 'Users'], params)
        new Policy(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'PolicyName'    : PolicyName,
                'PolicyDocument': PolicyDocument
            ]
        ]
        if (Roles) map['Properties']['Roles'] = Roles
        if (Groups) map['Properties']['Groups'] = Groups
        if (Users) map['Properties']['Users'] = Users
        map
    }

}
