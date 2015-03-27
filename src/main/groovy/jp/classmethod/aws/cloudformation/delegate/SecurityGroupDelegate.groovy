package jp.classmethod.aws.cloudformation.delegate

import jp.classmethod.aws.cloudformation.ec2.SecurityGroup
import jp.classmethod.aws.cloudformation.ec2.SecurityGroupIngress

/**
 * Created by watanabeshuji on 2015/03/27.
 */
class SecurityGroupDelegate {

    SecurityGroup securityGroup

    SecurityGroupDelegate(SecurityGroup securityGroup) {
        this.securityGroup = securityGroup
    }

    void securityGroupIngress(Map params) {
        this.securityGroup.SecurityGroupIngress << new SecurityGroupIngress(params)
    }

}
