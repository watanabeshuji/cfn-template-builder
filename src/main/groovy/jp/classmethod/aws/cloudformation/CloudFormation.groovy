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
    List Outputs = []
    Map binding = [:]

    def getResourcesSummary() {
        def summary = []
        Resources.each {
            summary << [Name: it.id, Type: it.TYPE]
        }
        summary
    }

    def toJson() {
        def json = new JsonBuilder()
        json {
            'AWSTemplateFormatVersion' '2010-09-09'
            if (Description) 'Description' Description
            if (!Mappings.isEmpty()) 'Mappings' Mappings
            if (!Parameters.isEmpty()) 'Parameters' Parameters
            if (!resources.isEmpty()) 'Resources' Resources.collectEntries { [it.id, it.toResourceMap()] }
            if (!Outputs.isEmpty()) 'Outputs' Outputs.collectEntries { [it.id, it.toMap()] }
        }
        json
    }

    @Override
    def String toString() {
        toJson().toString()
    }

    def toPrettyString() {
        toJson().toPrettyString()
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
        load(path, [:])
    }

    static CloudFormation load(Path path, Map binding) {
        DSLSupport.load(path, binding)
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
