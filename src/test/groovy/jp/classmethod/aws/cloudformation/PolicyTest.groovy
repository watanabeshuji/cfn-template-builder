package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class PolicyTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("PolicyTest_default.csv").getFile())
        def actual = Policy.load(input)
        assert actual == [
            new Policy(id: 'RootPolicy', Name: 'RootPolicy', PolicyName: 'root',
                       PolicyDocument: [
                           Version: '2012-10-17',
                           Statement: [
                               [Action: '*', Effect: 'Allow', Resource: '*']
                           ]
                       ],
                       Roles: [[Ref: 'Web']], Groups:[], Users: [[Ref:'Admin'],[Ref: 'PowerUser']]
            )
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new Policy(id: 'RootPolicy', Name: 'RootPolicy', PolicyName: 'root',
                PolicyDocument: [
                        Version: '2012-10-17',
                        Statement: [
                                [Action: '*', Effect: 'Allow', Resource: '*']
                        ]
                ],
                Roles: [[Ref: 'Web']], Groups:[], Users: [[Ref:'Admin'],[Ref: 'PowerUser']]
        )
        def expected = [
            "RootPolicy": [
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
        ]
        assert sut.toResourceMap() == expected
    }


}
