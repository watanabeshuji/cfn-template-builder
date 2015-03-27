package jp.classmethod.aws.cloudformation.iam

import jp.classmethod.aws.cloudformation.iam.Role
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RoleTest {

    @Test
    void "toResourceMap"() {
        def sut = new Role(id: 'RootRole')
        def expected = [
            'Type': 'AWS::IAM::Role',
            'Properties': [
                'Path': '/',
                'AssumeRolePolicyDocument': [
                    Version: '2012-10-17',
                    Statement: [
                        [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
