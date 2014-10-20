package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/10/17.
 */
class ParameterTest {

    @Test
    void "load"() {
        File input = new File(getClass().getResource("Parameters.csv").getFile())
        def actual = Parameter.load(input)
        assert actual == [
            new Parameter(
                Name: 'InstanceType',
                Type: 'String',
                Default: 't2.small',
                AllowedValues: ['t2.small', 'm3.large'],
                Description: 'Instance type for EC2 Instance'
            ),
            new Parameter(
                Name: 'Env',
                Type: 'String',
                Default: null,
                AllowedValues: ['blue', 'green'],
                Description: 'Environment for deployment(Blue/Green)'
            )
        ]
    }


    @Test
    void "toResourceMap"() {
        def sut = new Parameter(
            Name: 'InstanceType',
            Type: 'String',
            Default: 't2.small',
            AllowedValues: ['t2.small', 'm3.large'],
            Description: 'Instance type for EC2 Instance'
        )
        def expected = [
            "InstanceType": [
                'Type': 'String',
                'Default': 't2.small',
                'AllowedValues': ['t2.small', 'm3.large'],
                'Description': 'Instance type for EC2 Instance'
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
