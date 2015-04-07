package jp.classmethod.aws.cloudformation.iam

import jp.classmethod.aws.cloudformation.util.ValidErrorException
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
            new Role(
                id: 'Role',
                'Path': '/',
                'AssumeRolePolicyDocument': [
                    Version  : '2012-10-17',
                    Statement: [
                        [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
                    ]
                ]
            )
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = Role.newInstance(id: 'RootRole')
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


    @Test
    void "refIds"() {
        def sut = Role.newInstance(id: 'Web')
        assert sut.refIds == []
    }


    @Test(expected = ValidErrorException)
    void "id 必須"() {
        Role.newInstance([:])
    }
}
