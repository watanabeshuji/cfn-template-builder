package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.util.ValidErrorException
import org.junit.Test

import java.nio.file.Path

import static jp.classmethod.aws.cloudformation.testing.TestSupport.getPath

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InternetGatewayTest {

    @Test
    void "load internetGateway.groovy"() {
        Path input = getPath("/templates/resources/internetGateway.groovy")
        def actual = InternetGateway.load(input)
        assert actual == [
            new InternetGateway(id: 'InternetGateway'),
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new InternetGateway(id: 'InternetGateway')
        def expected = [
            'Type'      : 'AWS::EC2::InternetGateway',
            'Properties': [
                'Tags': [
                    ['Key': 'Name', 'Value': 'InternetGateway'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

    @Test
    void "refIds"() {
        def sut = new InternetGateway(id: 'InternetGateway')
        assert sut.refIds() == []
    }

    @Test(expected = ValidErrorException)
    void "未対応のパラメータを含む"() {
        InternetGateway.newInstance(id: "InternetGateway", xxx: "X")
    }

    @Test(expected = ValidErrorException)
    void "id必須"() {
        InternetGateway.newInstance([:])
    }

}

