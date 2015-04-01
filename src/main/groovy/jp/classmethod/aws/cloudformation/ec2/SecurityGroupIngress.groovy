package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical

/**
 * AWS::EC2::SecurityGroupIngress
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group-ingress.html
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class SecurityGroupIngress {

    final def Type = 'AWS::EC2::SecurityGroupIngress'
    def IpProtocol
    def FromPort
    def ToPort
    def CidrIp

    def toInlineMap() {
        def map = [
            'IpProtocol': this.IpProtocol
        ]
        if (this.FromPort) map['FromPort'] = this.FromPort
        if (this.ToPort) map['ToPort'] = this.ToPort
        if (this.CidrIp) map['CidrIp'] = this.CidrIp
        map
    }

}
