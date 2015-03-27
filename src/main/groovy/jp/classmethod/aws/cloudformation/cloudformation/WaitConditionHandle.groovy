package jp.classmethod.aws.cloudformation.cloudformation

import groovy.transform.Canonical

/**
 * AWS::CloudFormation::WaitConditionHandle
 *
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitConditionHandle {

    def id

    def WaitConditionHandle() {}

    def toResourceMap() {
        [
            'Type': 'AWS::CloudFormation::WaitConditionHandle'
        ]
    }

}
