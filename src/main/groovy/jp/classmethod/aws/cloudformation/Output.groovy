package jp.classmethod.aws.cloudformation

import groovy.transform.Canonical

import static jp.classmethod.aws.cloudformation.util.Params.convert

/**
 * Created by watanabeshuji on 2015/04/20.
 */
@Canonical
class Output {

    def id
    def Value
    def Description

    def Output withId(id) {
        this.id = id
        this
    }

    def toMap() {
        def map = [
            Value: Value
        ]
        if (Description) map["Description"] = Description
        map
    }

    static Output newInstance(id, Map params) {
        convert(params)
        new Output(params).withId(id)
    }

}
