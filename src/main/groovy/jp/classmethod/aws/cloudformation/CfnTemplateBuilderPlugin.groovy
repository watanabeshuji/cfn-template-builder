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
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Meta.txt'), Paths.get(cfnDir, 'Meta.txt'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Mappings/AMI'), Paths.get(cfnDir, 'Mappings', 'AMI'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/templates/Mappings/Common'), Paths.get(cfnDir, 'Mappings', 'Common'))
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
        project.tasks.cfnNew.description = "Create new cfn-template-builder file. Option: -PcfnType=(VPC|InternetGateway|Routing|SecurityGroup|EC2|ALL) -PcfnDir=[cfnDir]."
        project.tasks.cfnBuild.description = "Build CloudFormation Template file. Option: -PcfnDir=[cfnDir]."
        project.tasks.cfnClean.description = "Cleanup cfn directory. Option: -PcfnDir=[cfnDir]."
        project.tasks.generateTemplate.description = "Deprecated task. Please use 'cfnBuild' task"
        // Packer tasks TODO 後でプラグインを分離すべき
        project.ext.amiDir = (project.hasProperty('amiDir')) ? project.getProperty('amiDir') : "./ami"
        project.task('amiInit') << {
            println 'CloudFormation Builder'
            def amiDir = project.ext.amiDir
            if (Paths.get(amiDir).toFile().exists()) {
                println ""
                println "Sorry!!"
                println "Already exist ami directory: " + amiDir
                return
            }
            Files.createDirectory(Paths.get(amiDir))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/variables.json.sample'), Paths.get(amiDir, 'variables.json.sample'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/variables.json.sample'), Paths.get(amiDir, 'variables.json'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/Example.json'), Paths.get(amiDir, 'Example.json'))
            Files.createDirectory(Paths.get(amiDir, 'Example'))
            Files.copy(CfnTemplateBuilderPlugin.class.getResourceAsStream('/ami/ansible/setup.yml'), Paths.get(amiDir, 'Example', 'setup.yml'))
        }
        project.task('amiClean') << {
            println 'CloudFormation Builder'
            println 'Cleanup ami directory....'
            def amiDir = project.ext.amiDir
            Paths.get(amiDir).toFile().deleteDir()
        }
        project.tasks.amiInit.group = TASK_NAME
        project.tasks.amiClean.group = TASK_NAME
        project.tasks.amiInit.description = "Initialize cfn-template-builder. Create ami directory. Option: -PamiDir=[amiDir]."
        project.tasks.amiInit.description = "Cleanup ami directory. Option: -PamiDir=[amiDir]."
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
            case 'SecurityGroup':
                copyTemplateFile('/templates/SecurityGroup/default.csv', Paths.get(cfnDir, '00_SecurityGroup.csv'))
                break
            case 'EC2':
                copyTemplateFile('/templates/EC2/default.csv', Paths.get(cfnDir, '00_EC2.csv'))
                break
            case 'RDS':
                copyTemplateFile('/templates/RDS/default_DBSubnetGroup.csv', Paths.get(cfnDir, '00_DBSubnetGroup.csv'))
                copyTemplateFile('/templates/RDS/default_DBInstance.csv', Paths.get(cfnDir, '00_DBInstance.csv'))
                break
            case 'ALL':
                [
                    ['/templates/VPC/default.csv', '01_VPC.csv'],
                    ['/templates/InternetGateway/default.csv', '02_InternetGateway.csv'],
                    ['/templates/Subnet/default.csv', '03_Subnet.csv'],
                    ['/templates/Routing/RouteTable_default.csv', '04_RouteTable.csv'],
                    ['/templates/Routing/Route_default.csv', '05_Route.csv'],
                    ['/templates/Routing/SubnetRouteTableAssociation_default.csv', '06_SubnetRouteTableAssociation.csv'],
                    ['/templates/SecurityGroup/default.csv', '07_SecurityGroup.csv'],
                    ['/templates/EC2/default.csv', '11_Instance.csv'],
                    ['/templates/RDS/default_DBSubnetGroup.csv', '21_DBSubnetGroup.csv'],
                    ['/templates/RDS/default_DBInstance.csv', '22_DBInstance.csv'],
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

