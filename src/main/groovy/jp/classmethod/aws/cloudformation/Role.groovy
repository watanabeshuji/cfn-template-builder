package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Role {
    def id
    def Name
    def Path = '/'
    def AssumeRolePolicyDocument = [
        Version: '2012-10-17',
        Statement: [
            [Effect: 'Allow', Principal: [Service: ['ec2.amazonaws.com']], Action: ['sts:AssumeRole']]
        ]
    ]

    def Role() {
    }

    def Role(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
    }

    def toResourceMap() {
        def result = [
            (id): [
                'Type': 'AWS::IAM::Role',
                'Properties': [
                    'AssumeRolePolicyDocument': AssumeRolePolicyDocument,
                    'Path': Path
                ]
            ]
        ]
        result
    }

    static def load(File file) {
        Util.load(file, {new Role(it)})
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Role r -> o << r.toResourceMap() } )
    }

}
