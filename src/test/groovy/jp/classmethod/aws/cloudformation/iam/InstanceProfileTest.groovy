package jp.classmethod.aws.cloudformation.iam

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InstanceProfileTest {

    @Test
    void "toResourceMap"() {
        def sut = new InstanceProfile(id: 'WebInstanceProfile', Path: '/', Roles: [[Ref:'Web'], [Ref:'EC2PowerUser']])
        def expected = [
            'Type': 'AWS::IAM::InstanceProfile',
            'Properties': [
                'Path': '/',
                'Roles': [[Ref:'Web'], [Ref:'EC2PowerUser']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
