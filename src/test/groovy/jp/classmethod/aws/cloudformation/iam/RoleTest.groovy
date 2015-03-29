package jp.classmethod.aws.cloudformation.iam

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RoleTest {

    @Test
    void "load role.groovy"() {
        Path input = getPath("/templates/resources/role.groovy")
        def actual = Role.load(input)
        assert actual == [
            new Role(id: 'Role')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new Role(id: 'RootRole')
        def expected = [
            'Type'      : 'AWS::IAM::Role',
            'Properties': [
                'Path'                    : '/',
                'AssumeRolePolicyDocument': [
                    Version  : '2012-10-17',
                    Statement: [
                        [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
                    ]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
