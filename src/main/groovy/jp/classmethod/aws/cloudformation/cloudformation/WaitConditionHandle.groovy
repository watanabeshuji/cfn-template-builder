package jp.classmethod.aws.cloudformation.cloudformation

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::CloudFormation::WaitConditionHandle
 *
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitConditionHandle extends Resource {

    def id

    def WaitConditionHandle() {}

    def toResourceMap() {
        [
            'Type': 'AWS::CloudFormation::WaitConditionHandle'
        ]
    }

}
