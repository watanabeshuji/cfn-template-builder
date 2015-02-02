package jp.classmethod.aws.cloudformation

import org.junit.Test

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class EIPTest {

    @Test
    void "default.csvのload"() {
        File input = new File(getClass().getResource("EIPTest_default.csv").getFile())
        def actual = EIP.load(input)
        assert actual == [
            new EIP(id: 'Web1EIP', Name: 'Web1EIP'),
            new EIP(id: 'Web2EIP', Name: 'Web2EIP')
        ]
    }

    @Test
    void "toResourceMap"() {
        def sut = new EIP(id: 'Web1EIP', Name: 'web1-eip')
        def expected = [
            "Web1EIP": [
                'Type': 'AWS::EC2::EIP',
                'Properties': [
                    'Domain': 'vpc'
                ]
            ]
        ]
        assert sut.toResourceMap() == expected
    }

}
