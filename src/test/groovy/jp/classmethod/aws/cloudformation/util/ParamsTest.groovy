package jp.classmethod.aws.cloudformation.util

import org.junit.Test

/**
 * Created by watanabeshuji on 2015/04/05.
 */
class ParamsTest {

    @Test
    def void "convertUserData"() {
        def userData = """\
#! /bin/sh

yum -y update
"""
        def actual = Params.convertUserData(userData)
        assert actual == [
            "#! /bin/sh\\n",
            "\\n",
            "yum -y update\\n"
        ]
    }

    @Test
    def void "convertUserData Ref有"() {
        def userData = """\
#! /bin/bash -v

# Install packages
/opt/aws/bin/cfn-init -s [Ref:AWS::StackId] -r WebServer --region [Ref:AWS::Region]
"""
        def actual = Params.convertUserData(userData)
        def expected = [
            "#! /bin/bash -v\\n",
            "\\n",
            "# Install packages\\n",
            "/opt/aws/bin/cfn-init -s ", [Ref: "AWS::StackId"], " -r WebServer --region ", [Ref: "AWS::Region"], "\\n"
        ]
        assert actual == expected
    }
}
