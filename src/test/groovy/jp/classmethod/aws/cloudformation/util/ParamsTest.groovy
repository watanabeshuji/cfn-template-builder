package jp.classmethod.aws.cloudformation.util

import org.junit.Test

/**
 * Created by watanabeshuji on 2015/04/05.
 */
class ParamsTest {

    @Test
    def void "convert"() {
        assert Params.convert([ Key: "Value" ]) == [ Key: "Value" ]
    }

    @Test
    def void "convert Ref"() {
        assert Params.convert([ Key: "Ref:Value" ]) == [ Key: [Ref: "Value" ]]
    }

    @Test
    def void "convert FindInMap"() {
        assert Params.convert([ Key: "FindInMap:Key1:Key2:Key3" ]) == [ Key: ["Fn::FindInMap": ["Key1", "Key2", "Key3"] ]]
    }


    @Test
    def void "convert GetAtt"() {
        assert Params.convert([ Key: "GetAtt:Target:Attr" ]) == [ Key: ["Fn::GetAtt": ["Target", "Attr"] ]]
    }

    @Test
    def void "convert all types"() {
        assert Params.convert([
            Key1: "Value",
            Key2: "Ref:Value",
            Key3: "FindInMap:Key1:Key2:Key3",
            Key4: "GetAtt:Target:Attr"
        ]) == [
            Key1: "Value",
            Key2: [Ref: "Value" ],
            Key3: ["Fn::FindInMap": ["Key1", "Key2", "Key3"]],
            Key4: ["Fn::GetAtt": ["Target", "Attr"] ]
        ]
    }

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
