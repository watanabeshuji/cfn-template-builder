package jp.classmethod.aws.cloudformation.ec2

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

import static jp.classmethod.aws.cloudformation.util.Params.convert
import static jp.classmethod.aws.cloudformation.util.Valid.checkKeys
import static jp.classmethod.aws.cloudformation.util.Valid.require

/**
 * AWS::EC2::SecurityGroupIngress
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group-ingress.html
 * Created by watanabeshuji on 2015/03/27.
 */
@Canonical
class SecurityGroupIngress extends Resource {


    static final def TYPE = 'AWS::EC2::SecurityGroupIngress'
    static final def DESC = '''\
AWS::EC2::Subnet
http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-ec2-security-group-ingress.html

[Required Params]
- id
- IpProtocol

[Optional Params]
- FromPort
- ToPort
- CidrIp

[Sample]
'''

    def IpProtocol
    def FromPort
    def ToPort
    def CidrIp

    static SecurityGroupIngress newInstance(Map params) {
        convert(params)
        checkKeys(SecurityGroupIngress, params, ['id', 'IpProtocol', 'ToPort', 'FromPort', 'CidrIp'])
//        logicalId(SecurityGroupIngress, params)
        require(SecurityGroupIngress, 'IpProtocol', params)
        new SecurityGroupIngress(params).withRefIds(params)
    }

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
