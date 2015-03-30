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
        def dir = project.ext.cfnDir
        def output = (project.hasProperty('output')) ? project.getProperty('output') as Boolean : true
        def dryRun = (project.hasProperty('dryRun')) ? project.getProperty('dryRun') as Boolean : false
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
        project.task('cfnValidate') << {
            println 'CloudFormation Builder'
            println 'Validate DSL as template....'
            CloudFormation cfn = load(dir)
            validateTemplate(cfn)
            println "Success!"
        }
        project.task('cfnBuild') << {
            println 'CloudFormation Builder'
            CloudFormation cfn = load(dir)
            cfn.doValidate()
            def json = cfn.toPrettyString()
            if (output) println json
            def out = new File(dir, "cfn.template")
            out.write(json)
            println "File generated:  ${out.absolutePath}"
            println "Name\tType"
            cfn.resourcesSummary.each { println "${it.Name}\t${it.Type}" }
        }
        project.task('cfnClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup cfn directory....'
            def cfnDir = project.ext.cfnDir
            Paths.get(cfnDir).toFile().deleteDir()
        }
        project.tasks.cfnInit.group = TASK_NAME
        project.tasks.cfnClean.group = TASK_NAME
        project.tasks.cfnValidate.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.cfnInit.description = "Initialize cfn-template-builder. Create cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnValidate.description = "Validate CloudFormation DSL. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation DSL. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
    }

    def CloudFormation load(String dir) {
        println "Load from $dir"
        CloudFormation.load(Paths.get(dir, "cfn.groovy"))
    }

    def validateTemplate(CloudFormation cfn) {
        CloudFormationClient client = new CloudFormationClient()
        client.validateTemplate(cfn.toString())
    }
}

