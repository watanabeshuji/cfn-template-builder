package jp.classmethod.aws.cloudformation.iam

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::IAM::Policy
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-iam-policy.html
 *
 * Created by watanabeshuji on 2015/02/02.
 */
@Canonical
class Policy extends Resource {

    def id
    final def Type = 'AWS::IAM::Policy'
    def PolicyName
    def PolicyDocument
    def Roles = []
    def Groups = []
    def Users = []

    def Policy() {
    }

    def toResourceMap() {
        def result = [
            'Type'      : Type,
            'Properties': [
                'PolicyName'    : PolicyName,
                'PolicyDocument': PolicyDocument
            ]
        ]
        if (!Roles.isEmpty()) result['Properties']['Roles'] = Roles
        if (!Groups.isEmpty()) result['Properties']['Groups'] = Groups
        if (!Users.isEmpty()) result['Properties']['Users'] = Users
        result
    }

}
