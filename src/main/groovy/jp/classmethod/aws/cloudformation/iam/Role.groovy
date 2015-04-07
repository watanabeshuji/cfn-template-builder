package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Role extends Resource {

    static final def TYPE = 'AWS::IAM::Role'
    static def DESC = '''\
AWS::IAM::Role
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-role.html

[Required Params]
- id
- AssumeRolePolicyDocument  (Default=[ Version: '2012-10-17', Statement: [ [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']] ]])
- Path (Default="/")

[Optional Params]
-

[Sample]
resources {
    role id: "Role"
}
'''
    def id
    def Path
    def AssumeRolePolicyDocument
    def Policies

    static Role newInstance(Map params) {
        convert(params)
        checkKeys(Role, params, ['id', 'Path', 'AssumeRolePolicyDocument', 'Policies'])
        if (params['Path'] == null) params['Path'] = '/'
        if (params['AssumeRolePolicyDocument'] == null) params['AssumeRolePolicyDocument'] = [
            Version  : '2012-10-17',
            Statement: [
                [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
            ]
        ]
        logicalId(Role, params)
        new Role(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'AssumeRolePolicyDocument': AssumeRolePolicyDocument,
                'Path'                    : Path
            ]
        ]
        if (Policies) map['Properties']['Policies'] = Policies
        map
    }


}
