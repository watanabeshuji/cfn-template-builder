package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InstanceProfileTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("InstanceProfileTest_default.csv").getFile())
        def actual = InstanceProfile.load(input)
        assert actual == [
            new InstanceProfile(id: 'WebInstanceProfile', Name: 'WebInstanceProfile', Path: '/', Roles: [[Ref:'Web'], [Ref:'EC2PowerUser']])
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new InstanceProfile(id: 'WebInstanceProfile', Name: 'WebInstanceProfile', Path: '/', Roles: [[Ref:'Web'], [Ref:'EC2PowerUser']])
        def expected = [
            "WebInstanceProfile": [
                'Type': 'AWS::IAM::InstanceProfile',
                'Properties': [
                    'Path': '/',
                    'Roles': [[Ref:'Web'], [Ref:'EC2PowerUser']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
