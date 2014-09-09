package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/08/19.
 */
class Meta extends LinkedHashMap {


    def add(lines) {
        this.put(lines[0].trim(), lines[1].trim())
    }

    static Meta load(file) {
        def result = new Meta()
        if (!file.exists()) return result
        file.eachLine { line ->
            result.add(line.split('='))
        }
        result
    }
}
