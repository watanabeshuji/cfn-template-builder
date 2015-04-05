package jp.classmethod.aws.cloudformation.cloudformation

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.*

/**
 * AWS::CloudFormation::WaitCondition
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-waitcondition.html
 *
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitCondition extends Resource {
    static final def TYPE = 'AWS::CloudFormation::WaitCondition'
    static def DESC = '''\
AWS::CloudFormation::WaitCondition
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-waitcondition.html

[Required Params]
- id
- Handle
- Timeout

[Optional Params]

[Sample]
resources {
    waitCondition id: "WebServerWaitCondition", Handle: [Ref: "WebServerWaitHandle"], Timeout: "1000"
}
'''
    def id
    def Handle
    def Timeout

    static WaitCondition newInstance(Map params) {
        convert(params)
        checkKeys(WaitCondition, params, ['id', 'Handle', 'Timeout'])
        logicalId(WaitCondition, params)
        require(WaitCondition, ['Handle', 'Timeout'], params)
        new WaitCondition(params).withRefIds(params)
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'Handle' : Handle,
                'Timeout': this.Timeout
            ]
        ]
        map
    }
}
