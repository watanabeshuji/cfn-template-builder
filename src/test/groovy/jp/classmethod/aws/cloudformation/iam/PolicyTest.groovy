package jp.classmethod.aws.cloudformation.iam

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path
import java.nio.file.Paths

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class PolicyTest {

    @Test
    void "load policy.groovy"() {
        Path input = getPath("/templates/resources/policy.groovy")
        def actual = Policy.load(input)
        def doc = [
            "Version"  : "2012-10-17",
            "Statement": [
                ["Effect": "Allow", "Action": "*", "Resource": "*"]
            ]
        ]
        def expected = [
            new Policy(id: 'EC2Policy', PolicyName: "EC2", PolicyDocument: doc, Roles: [[Ref: "EC2Role"]])
        ]
        assert actual == expected
    }

    @Test
    void "load policy_refInRoles.groovy"() {
        Path input = Paths.get(getClass().getResource("policy_refInRoles.groovy").getPath())
        def actual = Policy.load(input)
        def doc = [
            "Version"  : "2012-10-17",
            "Statement": [
                ["Effect": "Allow", "Action": "*", "Resource": "*"]
            ]
        ]
        def expected = [
            new Policy(id: 'EC2Policy', PolicyName: "EC2", PolicyDocument: doc, Roles: [[Ref: "EC2Role"]])
        ]
        assert actual == expected
    }


    @Test
    void "toResourceMap"() {
        def sut = Policy.newInstance(id: 'RootPolicy', PolicyName: 'root',
            PolicyDocument: [
                Version  : '2012-10-17',
                Statement: [
                    [Action: '*', Effect: 'Allow', Resource: '*']
                ]
            ],
            Roles: ["Ref:Web"], Users: ["Ref:Admin", "Ref:PowerUser"]
        )
        def expected = [
            'Type'      : 'AWS::IAM::Policy',
            'Properties': [
                'PolicyName'    : 'root',
                'PolicyDocument': [
                    Version  : '2012-10-17',
                    Statement: [[Action: '*', Effect: 'Allow', Resource: '*']]
                ],
                'Roles'         : [[Ref: 'Web']],
                'Users'         : [[Ref: 'Admin'], [Ref: 'PowerUser']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = Policy.newInstance(id: 'RootPolicy', PolicyName: 'root',
            PolicyDocument: [
                Version  : '2012-10-17',
                Statement: [
                    [Action: '*', Effect: 'Allow', Resource: '*']
                ]
            ],
            Roles: ["Ref:Web"], Users: ["Ref:Admin", "Ref:PowerUser"]
        )
        assert sut.refIds == ['Web', 'Admin', 'PowerUser']
    }

    @Test(expected = ValidErrorException)
    void "id 必須"() {
        Policy.newInstance(PolicyName: 'root',
            PolicyDocument: [
                Version  : '2012-10-17',
                Statement: [
                    [Action: '*', Effect: 'Allow', Resource: '*']
                ]
            ],
            Roles: ["Ref:Web"], Users: ["Ref:Admin", "Ref:PowerUser"]
        )
    }

    @Test(expected = ValidErrorException)
    void "PolicyName 必須"() {
        Policy.newInstance(id: 'RootPolicy',
            PolicyDocument: [
                Version  : '2012-10-17',
                Statement: [
                    [Action: '*', Effect: 'Allow', Resource: '*']
                ]
            ],
            Roles: ["Ref:Web"], Users: ["Ref:Admin", "Ref:PowerUser"]
        )
    }

    @Test(expected = ValidErrorException)
    void "PolicyDocument 必須"() {
        Policy.newInstance(id: 'RootPolicy', PolicyName: 'root',
            Roles: ["Ref:Web"], Users: ["Ref:Admin", "Ref:PowerUser"]
        )
    }

    @Test(expected = ValidErrorException)
    void "Roles/Users/Groups 必須"() {
        Policy.newInstance(id: 'RootPolicy', PolicyName: 'root',
            PolicyDocument: [
                Version  : '2012-10-17',
                Statement: [
                    [Action: '*', Effect: 'Allow', Resource: '*']
                ]
            ]
        )
    }
}
