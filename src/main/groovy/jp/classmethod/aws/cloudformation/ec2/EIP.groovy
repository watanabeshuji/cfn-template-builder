package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::EC2::EIP
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class EIP extends Resource {

    final def Type = 'AWS::EC2::EIP'
    def id
    def InstanceId

    def EIP() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : Type,
            'Properties': [
                'Domain': 'vpc'
            ]
        ]
        if (InstanceId) map['Properties']['InstanceId'] = [Ref: InstanceId]
        map
    }

}
