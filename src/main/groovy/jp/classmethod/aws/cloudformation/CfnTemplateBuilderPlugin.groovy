package jp.classmethod.aws.cloudformation

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.ext.cfnDir = "./cfn"
        project.ext.printTemplateJSON = true
        project.task('generateTemplate') << {
            println 'CloudFormation Builder'
            def dir = project.ext.cfnDir
            def printTemplateJSON = project.ext.printTemplateJSON
            println "Load from $dir"
            def template = Template.build(dir)
            if (printTemplateJSON) println template.toPrettyString()
            def out = new File(dir, "cfn.template")
            out.write(template.toPrettyString())
            println "File generated:  ${out.absolutePath}"
        }
    }
}

