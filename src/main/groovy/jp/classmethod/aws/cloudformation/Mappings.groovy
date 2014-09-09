package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/08/14.
 */
class Mappings extends LinkedHashMap<String, String> {

    static Mappings load(File dir) {
        def mappings = new Mappings()
        if (!dir.exists()) return mappings
        dir.listFiles().each { file ->
            def name = file.name
            def meta
            if (mappings[name] == null) mappings[name] = [:]
            file.eachLine { line, num ->
                if (num == 1) {
                    meta = new SourceMeta(line.split(','))
                } else {
                    def source = meta.newSource(line.split(','))
                    def key = source.value('Key')
                    if (mappings[name][key] == null) mappings[name][key] = [:]
                    mappings[name][key][source.value('Name')] = source.value('Value')
                }
            }
        }
        mappings
    }
}
