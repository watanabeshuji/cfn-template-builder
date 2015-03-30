package jp.classmethod.aws.cloudformation

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by watanabeshuji on 2014/09/09.
 */
class CfnTemplateBuilderPlugin implements Plugin<Project> {

    static final String TASK_NAME = 'AWS tasks'

    void apply(Project project) {
        project.ext.cfnDir = (project.hasProperty('cfnDir')) ? project.getProperty('cfnDir') : "./cfn"
        project.task('cfnInit') << {
            println 'CloudFormation Builder'
            def cfnDir = project.ext.cfnDir
            if (Paths.get(cfnDir).toFile().exists()) {
                println ""
                println "Sorry!!"
                println "Already exist cfn directory: " + cfnDir
                return
            }
            Files.createDirectory(Paths.get(cfnDir))
            println CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/vpc.groovy')
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/vpc.groovy'), Paths.get(cfnDir, 'cfn.groovy'))
        }
        project.task('cfnBuild') << {
            println 'CloudFormation Builder'
            def dir = project.ext.cfnDir
            def output = (project.hasProperty('output')) ? project.getProperty('output') as Boolean : true
            def dryRun = (project.hasProperty('dryRun')) ? project.getProperty('dryRun') as Boolean : false
            println "Load from $dir"
            def cfn = CloudFormation.load(Paths.get(dir, "cfn.groovy"))
            cfn.doValidate()
            def json = cfn.toPrettyString()
            if (output) println json
            def out = new File(dir, "cfn.template")
            out.write(json)
            println "File generated:  ${out.absolutePath}"
        }
        project.task('cfnClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup cfn directory....'
            def cfnDir = project.ext.cfnDir
            Paths.get(cfnDir).toFile().deleteDir()
        }
        project.tasks.cfnInit.group = TASK_NAME
        project.tasks.cfnClean.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.cfnInit.description = "Initialize cfn-template-builder. Create cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation Template file. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
    }
}

