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

    def toResourceMap() {
        def map = [
            (this.Name): [
                'Type': this.Type
            ]
        ]
        if (Default) map[Name]['Default'] = Default
        if (AllowedValues && !AllowedValues.isEmpty()) map[Name]['AllowedValues'] = AllowedValues
        if (Description) map[Name]['Description'] = Description
        map
    }


    static Parameter[] load(File file) {
        Util.load(file, {new Parameter(it)})
    }
}