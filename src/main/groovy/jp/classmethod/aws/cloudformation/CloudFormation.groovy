package jp.classmethod.aws.cloudformation

import groovy.json.JsonBuilder
import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.dsl.CloudFormationDSL

import java.nio.file.Path

/**
 * Created by watanabeshuji on 2015/03/26.
 */
@Canonical
class CloudFormation {
    String AWSTemplateFormatVersion = "2010-09-09"
    String Description
    Map Mappings = [:]
    Map Parameters = [:]
    List Resources = []

    def toPrettyString() {
        def json = new JsonBuilder()
        json {
            'AWSTemplateFormatVersion' '2010-09-09'
            if (Description) 'Description' Description
            if (!Mappings.isEmpty()) 'Mappings' Mappings
            if (!Parameters.isEmpty()) 'Parameters' Parameters
            if (!resources.isEmpty()) 'Resources' Resources.collectEntries { [it.id, it.toResourceMap()] }
        }
        json.toPrettyString()
    }

    static CloudFormation load(Path path) {
        CloudFormationDSL.load(path)
    }
}
