package jp.classmethod.aws.cloudformation.iam

import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InstanceProfileTest {

    @Test
    void "load instanceProfile.groovy"() {
        Path input = getPath("/templates/resources/instanceProfile.groovy")
        def actual = InstanceProfile.load(input)
        def expected = [
            new InstanceProfile(id: 'EC2InstanceProfile', Path: "/", Roles: [[Ref: "EC2Role"]])
        ]
        assert actual == expected
    }

    @Test
    void "toResourceMap"() {
        def sut = new InstanceProfile(id: 'WebInstanceProfile', Path: '/', Roles: [[Ref: 'Web'], [Ref: 'EC2PowerUser']])
        def expected = [
            'Type'      : 'AWS::IAM::InstanceProfile',
            'Properties': [
                'Path' : '/',
                'Roles': [[Ref: 'Web'], [Ref: 'EC2PowerUser']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }


}
