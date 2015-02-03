package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class RoleTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("RoleTest_default.csv").getFile())
        def actual = Role.load(input)
        assert actual == [
            new Role(id: 'RootRole', Name: 'RootRole', Path: '/',
                    AssumeRolePolicyDocument: [
                       Version: '2012-10-17',
                       Statement: [
                           [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
                       ]
                   ]
            )
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new Role(id: 'RootRole', Name: 'RootRole')
        def expected = [
            "RootRole": [
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
        ]
        assert sut.toResourceMap() == expected
    }


}
