package jp.classmethod.aws.cloudformation.ec2

import jp.classmethod.aws.cloudformation.ec2.InternetGateway
import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class InternetGatewayTest {

    @Test
    void "toResourceMap"() {
        def sut = new InternetGateway(id: 'InternetGateway')
        def expected = [
            'Type': 'AWS::EC2::InternetGateway',
            'Properties': [
                'Tags': [
                    ['Key': 'Name', 'Value': 'InternetGateway'],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId' ]]
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}

