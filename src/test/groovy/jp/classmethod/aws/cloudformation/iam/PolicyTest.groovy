package jp.classmethod.aws.cloudformation.iam

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class PolicyTest {

    @Test
    void "toResourceMap"() {
        def sut = new Policy(id: 'RootPolicy', PolicyName: 'root',
                PolicyDocument: [
                        Version: '2012-10-17',
                        Statement: [
                                [Action: '*', Effect: 'Allow', Resource: '*']
                        ]
                ],
                Roles: [[Ref: 'Web']], Groups:[], Users: [[Ref:'Admin'],[Ref: 'PowerUser']]
        )
        def expected = [
            'Type': 'AWS::IAM::Policy',
            'Properties': [
                'PolicyName': 'root',
                'PolicyDocument': [
                    Version: '2012-10-17',
                    Statement: [[Action: '*', Effect: 'Allow', Resource: '*']]
                ],
                'Roles': [[Ref: 'Web']],
                'Users': [[Ref:'Admin'],[Ref: 'PowerUser']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
