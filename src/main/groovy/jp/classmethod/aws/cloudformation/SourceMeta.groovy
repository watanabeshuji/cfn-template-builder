package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/08/13.
 */
class SourceMeta {

    def keys
    def SourceMeta(keys) {
        this.keys = keys.collect { it.trim() }
    }

    Source newSource(source) {
        new Source(source, this)
    }

    int indexOf(key) {
        this.keys.indexOf(key)
    }
}
