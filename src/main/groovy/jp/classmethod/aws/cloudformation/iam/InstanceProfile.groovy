package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::IAM::InstanceProfile
 *
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class InstanceProfile extends Resource {

    def id
    static final def TYPE = 'AWS::IAM::InstanceProfile'
    def Path
    def Roles

    def toResourceMap() {
        def result = [
            'Type'      : TYPE,
            'Properties': [
                'Path' : Path,
                'Roles': Roles
            ]
        ]
        result
    }

}
