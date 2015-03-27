package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical

/**
 * AWS::IAM::InstanceProfile
 *
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class InstanceProfile {

    def id
    def Path
    def Roles

    def toResourceMap() {
        def result = [
            'Type': 'AWS::IAM::InstanceProfile',
            'Properties': [
                'Path': Path,
                'Roles': Roles
            ]
        ]
        result
    }

}
