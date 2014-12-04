package jp.classmethod.aws.cloudformation

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.FileUtils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.ext.cfnDir = (project.hasProperty('cfnDir')) ? project.getProperty('cfnDir') : "./cfn"
        project.ext.printTemplateJSON =  (project.hasProperty('printTemplateJSON')) ? project.getProperty('printTemplateJSON') as Boolean : true
        project.task('initCfn') << {
            println 'CloudFormation Builder'
            println 'Create sample CSV files....'
            def cfnDir = project.ext.cfnDir
            if (Paths.get(cfnDir).toFile().exists()) {
                println ""
                println "Sorry!!"
                println "Already exist cfn directory: " + cfnDir
                return
            }
            Files.createDirectory(Paths.get(cfnDir))
            Files.createDirectory(Paths.get(cfnDir, 'Mappings'))
            Files.createDirectory(Paths.get(cfnDir, 'userdata'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('Meta.txt'), Paths.get(cfnDir, 'Meta.txt'))
        }
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
        project.tasks.initCfn.description = "Initialize cfn-template-builder. Create cfn directory."
        project.tasks.generateTemplate.description = "Generate CloudFormation Template file. Option: -PcfnDir=[cfnDir]."
    }
}

