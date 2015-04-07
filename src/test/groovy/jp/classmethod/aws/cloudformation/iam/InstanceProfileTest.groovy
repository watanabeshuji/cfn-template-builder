package jp.classmethod.aws.cloudformation.iam

import jp.classmethod.aws.cloudformation.util.ValidErrorException
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
        def sut = InstanceProfile.newInstance(id: 'WebInstanceProfile', Path: '/', Roles: ["Ref:Web", "Ref:EC2PowerUser"])
        def expected = [
            'Type'      : 'AWS::IAM::InstanceProfile',
            'Properties': [
                'Path' : '/',
                'Roles': [[Ref: 'Web'], [Ref: 'EC2PowerUser']]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = InstanceProfile.newInstance(id: 'WebInstanceProfile', Path: '/', Roles: ["Ref:Web", "Ref:EC2PowerUser"])
        assert sut.refIds == ['Web', 'EC2PowerUser']
    }


    @Test(expected = ValidErrorException)
    void "id 必須"() {
        InstanceProfile.newInstance(Path: '/', Roles: ["Ref:Web", "Ref:EC2PowerUser"])
    }

    @Test(expected = ValidErrorException)
    void "Path 必須"() {
        InstanceProfile.newInstance(id: 'WebInstanceProfile', Roles: ["Ref:Web", "Ref:EC2PowerUser"])
    }

    @Test(expected = ValidErrorException)
    void "Roles 必須"() {
        InstanceProfile.newInstance(id: 'WebInstanceProfile', Path: '/')
    }

    @Test(expected = ValidErrorException)
    void "Roles 必須(空)"() {
        InstanceProfile.newInstance(id: 'WebInstanceProfile', Path: '/', Roles: [])
    }
}
