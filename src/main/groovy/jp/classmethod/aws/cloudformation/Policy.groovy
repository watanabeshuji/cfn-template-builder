package jp.classmethod.aws.cloudformation

import groovy.json.JsonSlurper
import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Policy {
    def id
    def Name
    def PolicyName
    def PolicyDocument
    def Roles
    def Groups
    def Users

    def Policy() {
    }

    def Policy(Source source) {
        this.id = source.camelCase('Name')
        this.Name = source.value('Name')
        this.PolicyName = source.value('PolicyName')
        this.Roles = source.refList('Roles')
        this.Groups = source.refList('Groups')
        this.Users = source.refList('Users')
    }

    def toResourceMap() {
        def result = [
            (id): [
                'Type': 'AWS::IAM::Policy',
                'Properties': [
                    'PolicyName': PolicyName,
                    'PolicyDocument': PolicyDocument
                ]
            ]
        ]
        if (!Roles.isEmpty()) result[id]['Properties']['Roles'] = Roles
        if (!Groups.isEmpty()) result[id]['Properties']['Groups'] = Groups
        if (!Users.isEmpty()) result[id]['Properties']['Users'] = Users
        result
    }

    static def load(File file) {
        List<Policy> policies = Util.load(file, {new Policy(it)})
        File policyDir = new File(file.getParentFile().absolutePath, 'policy')
        policies.each {
            it.PolicyDocument = new JsonSlurper().parse(new File(policyDir, it.PolicyName + '.json'))
        }
        policies
    }

    static def inject(resources, file) {
        load(file).inject(resources, {o, Policy r -> o << r.toResourceMap() } )
    }

}
