package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

/**
 * Created by watanabeshuji on 2014/10/17.
 */
@Canonical
class Parameter {
    def Name
    def Type
    def Default
    def AllowedValues
    def Description

    def Parameter() {
    }

    def Parameter(Source source) {
        this.Name = source.value('Name')
        this.Type = source.value('Type')
        this.Default = source.value('Default')
        this.AllowedValues = source.list('AllowedValues')
        this.Description = source.value('Description')
    }

    def toMap() {
        def map = [
            'Type': this.Type
        ]
        if (Default) map['Default'] = Default
        if (AllowedValues && !AllowedValues.isEmpty()) map['AllowedValues'] = AllowedValues
        if (Description) map['Description'] = Description
        map
    }


    static def load(File file) {
        Util.load(file, {new Parameter(it)}).collectEntries {
            [it.Name, it.toMap()]
        }
    }
}