package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::SecurityGroupIngress
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group-ingress.html
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class SecurityGroupIngress {

    def IpProtocol
    def FromPort
    def ToPort
    def CidrIp

    def toInlineMap() {
        [
            'IpProtocol': this.IpProtocol,
            'FromPort': this.FromPort,
            'ToPort': this.ToPort,
            'CidrIp': this.CidrIp
        ]
    }

}
