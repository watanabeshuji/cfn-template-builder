package jp.classmethod.aws.cloudformation

import groovy.json.JsonBuilder
import groovy.transform.Canonical

import java.nio.file.Path
import java.nio.file.Paths

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

    def doValidate() {
        Map resources = [:]
        Resources.each { r ->
            def id = r.id
            if (resources.containsKey(id)) throw new InvalidResourceException("'$id' not unique in templates.")
            resources[id] = r
        }
    }

    static CloudFormation load(Path path) {
        DSLSupport.load(path)
    }

    static void main(String[] args) {
        if (args.length == 0) {
            def msg = '''\
Error: Rwquire an argument to dsl file path.
            '''
            System.err.println msg
            return
        }
        CloudFormation cfn = load(Paths.get(args[0]))
        println cfn.toPrettyString()
        if (1 < args.length) Paths.get(args[1]).text = cfn.toPrettyString()
    }
}
