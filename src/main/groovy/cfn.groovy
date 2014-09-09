import jp.classmethod.aws.cloudformation.*

def dir = args[0]
def printTemplateJSON = args[1] as Boolean
println "Load from $dir"
def template = Template.build(dir)
if (printTemplateJSON) println template.toPrettyString()
def out = new File(dir, "cfn.template")
out.write(template.toPrettyString())
println "File generated:  ${out.absolutePath}"
