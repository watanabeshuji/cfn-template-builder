package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId
import static jp.classmethod.aws.cloudformation.util.Valid.require
import static jp.classmethod.aws.cloudformation.util.Valid.requireOneOf

/**
 * AWS::IAM::InstanceProfile
 *
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class InstanceProfile extends Resource {

    static final def TYPE = 'AWS::IAM::InstanceProfile'
    static def DESC = '''\
AWS::IAM::InstanceProfile
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-instanceprofile.html

[Required Params]
- id
- Path
- Roles

[Optional Params]
none

[Sample]
resources {
    instanceProfile id: "EC2InstanceProfile", Path: "/", Roles: [[Ref: "EC2Role"]]
}
'''
    def id
    def Path
    def Roles

    static InstanceProfile newInstance(Map params) {
        convert(params)
        checkKeys(InstanceProfile, params, ['id', 'Path', 'Roles'])
        logicalId(InstanceProfile, params)
        require(InstanceProfile, ['Path', 'Roles'], params)
        new InstanceProfile(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Path' : Path,
                'Roles': Roles
            ]
        ]
        map
    }

}
