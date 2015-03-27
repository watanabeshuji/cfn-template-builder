package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::EIP
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class EIP extends Resource {
    def id
    def InstanceId

    def EIP() {
    }

    def toResourceMap() {
        def result = [
            'Type'      : 'AWS::EC2::EIP',
            'Properties': [
                'Domain': 'vpc'
            ]
        ]
        if (InstanceId) result['Properties']['InstanceId'] = [Ref: InstanceId]
        result
    }

}
