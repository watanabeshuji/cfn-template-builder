package jp.classmethod.aws.cloudformation.cloudformation

import groovy.transform.Canonical

/**
 * AWS::CloudFormation::WaitCondition
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-waitcondition.html
 *
 * Created by watanabeshuji on 2014/10/24.
 */
@Canonical
class WaitCondition {

    def id
    def Handle
    def Timeout

    def WaitCondition() {}

    def toResourceMap() {
        def result =[
            'Type': 'AWS::CloudFormation::WaitCondition',
            'Properties': [
                'Handle': Handle,
                'Timeout': this.Timeout
            ]
        ]
        result
    }
}