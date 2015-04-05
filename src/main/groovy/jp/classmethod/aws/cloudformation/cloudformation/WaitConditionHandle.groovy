package jp.classmethod.aws.cloudformation.cloudformation

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.logicalId

/**
 * AWS::CloudFormation::WaitConditionHandle
 *
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitConditionHandle extends Resource {
    static final def TYPE = 'AWS::CloudFormation::WaitConditionHandle'
    static def DESC = '''\
AWS::CloudFormation::WaitConditionHandle
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-waitconditionhandle.html

[Required Params]
- id

[Sample]
resources {
    waitConditionHandle id: "WebServerWaitConditionHandle"
}
'''

    def id

    static WaitConditionHandle newInstance(Map params) {
        convert(params)
        checkKeys(WaitConditionHandle, params, ['id'])
        logicalId(WaitConditionHandle, params)
        new WaitConditionHandle(params).withRefIds(params)
    }

    def toResourceMap() {
        [
            'Type': TYPE
        ]
    }

}
