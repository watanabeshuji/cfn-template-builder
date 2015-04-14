package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * Created by watanabeshuji on 2015/04/07.
 */
@Canonical
class User extends Resource {

    static final def TYPE = 'AWS::IAM::User'
    static def DESC = '''\
AWS::IAM::User
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-iam-user.html

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


}
