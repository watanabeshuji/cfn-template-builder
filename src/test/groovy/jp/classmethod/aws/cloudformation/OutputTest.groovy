package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class OutputTest {


    @Test
    void "load"() {
        File input = new File(getClass().getResource("Output.csv").getFile())
        def actual = Output.load(input)
        assert actual == [
            'WebServerIpAddress': [
                'Value': ['Ref': 'WebEip'],
                'Description': 'IP Address of Web Server'
            ],
            'WebServerAvailabilityZone': [
                'Value': ['Fn::GetAtt': ['Web', 'AvailabilityZone']],
                'Description': 'Availability Zone of Web Server'
            ]
        ]
    }

}
