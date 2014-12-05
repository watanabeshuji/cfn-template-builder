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
            Files.createDirectory(Paths.get(cfnDir, 'Mappings'))
            Files.createDirectory(Paths.get(cfnDir, 'userdata'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('Meta.txt'), Paths.get(cfnDir, 'Meta.txt'))
        }
        project.task('cfnNew') << {
            println 'CloudFormation Builder'
            println 'Create new CSV files....'
            def cfnDir = project.ext.cfnDir
            def cfnType = getProjectProperty(project, 'cfnType', 'ALL')
            createTemplateFile(cfnDir, cfnType)
        }
        project.task('cfnBuild') << {
            println 'CloudFormation Builder'
            def dir = project.ext.cfnDir
            def output = (project.hasProperty('output')) ? project.getProperty('output') as Boolean : true
            def dryRun = (project.hasProperty('dryRun')) ? project.getProperty('dryRun') as Boolean : false
            println "Load from $dir"
            def template = Template.build(dir)
            if (output) println template.toPrettyString()
            def out = new File(dir, "cfn.template")
            out.write(template.toPrettyString())
            println "File generated:  ${out.absolutePath}"
        }
        project.task('cfnClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup cfn directory....'
            def cfnDir = project.ext.cfnDir
            Paths.get(cfnDir).toFile().deleteDir()
        }
        project.task('generateTemplate') << {
            println 'CloudFormation Builder'
            println "Deprecated task. Please use 'cfnBuild' task"
        }
        project.tasks.cfnInit.group = TASK_NAME
        project.tasks.cfnNew.group = TASK_NAME
        project.tasks.cfnClean.group = TASK_NAME
        project.tasks.cfnBuild.group = TASK_NAME
        project.tasks.generateTemplate.group = TASK_NAME
        project.tasks.cfnInit.description = "Initialize cfn-template-builder. Create cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnNew.description = "Create new cfn-template-builder file. Option: -PcfnType=(VPC|InternetGateway|templates.Routing|ALL) -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation Template file. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.generateTemplate.description = "Deprecated task. Please use 'cfnBuild' task"
    }

    private getProjectProperty(Project project, String propertyName, defaultValue) {
        (project.hasProperty(propertyName)) ? project.getProperty(propertyName) : defaultValue
    }

    private createTemplateFile(String cfnDir, String cfnType) {
        switch (cfnType) {
            case 'VPC':
                copyTemplateFile('/templates/VPC/default.csv', Paths.get(cfnDir, '00_VPC.csv'))
                break
            case 'InternetGateway':
                copyTemplateFile('/templates/InternetGateway/default.csv', Paths.get(cfnDir, '00_InternetGateway.csv'))
                break
            case 'Subnet':
                copyTemplateFile('/templates/Subnet/default.csv', Paths.get(cfnDir, '00_Subnet.csv'))
                break
            case 'Routing':
                copyTemplateFile('/templates/Routing/RouteTable_default.csv', Paths.get(cfnDir, '00_RouteTable.csv'))
                copyTemplateFile('/templates/Routing/Route_default.csv', Paths.get(cfnDir, '00_Route.csv'))
                copyTemplateFile('/templates/Routing/SubnetRouteTableAssociation_default.csv', Paths.get(cfnDir, '00_SubnetRouteTableAssociation.csv'))
                break
            case 'ALL':
                [
                    ['/templates/VPC/default.csv', '01_VPC.csv'],
                    ['/templates/InternetGateway/default.csv', '02_InternetGateway.csv'],
                    ['/templates/Subnet/default.csv', '03_Subnet.csv'],
                    ['/templates/Routing/RouteTable_default.csv', '04_RouteTable.csv'],
                    ['/templates/Routing/Route_default.csv', '05_Route.csv'],
                    ['/templates/Routing/SubnetRouteTableAssociation_default.csv', '06_SubnetRouteTableAssociation.csv'],
                ].each { copyTemplateFile(it[0], Paths.get(cfnDir, it[1]))}
                break
        }
    }

    private copyTemplateFile(String resource, Path path) {
        if (path.toFile().exists()) {
            println "Already file exist: $path"
            return
        }
        Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream(resource), path)
        println "Create template file: $path"
    }
}

