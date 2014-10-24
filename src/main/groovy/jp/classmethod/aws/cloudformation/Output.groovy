package jp.classmethod.aws.cloudformation

/**
 * Created by watanabeshuji on 2014/10/24.
 */
class Output {

    def Name
    def Value
    def Description

    def Output() {
    }

    def Output(Source source) {
        this.Name = source.value('Name')
        this.Value = source.value('Value')
        this.Description = source.value('Description')
    }

    def toMap() {
        [
            'Value': Util.ref(this.Value),
            'Description': Description
        ]
    }

    static def load(File file) {
        Util.load(file, {new Output(it)}).collectEntries {
            [it.Name, it.toMap()]
        }
    }
}
