package jp.classmethod.aws.cloudformation.rds

import groovy.transform.Canonical
import jp.classmethod.aws.cloudformation.Resource

/**
 * AWS::RDS::DBInstance
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-rds-database-instance.html
 *
 * Created by watanabeshuji on 2014/09/10.
 */
@Canonical
class DBInstance extends Resource {

    def id
    static final def TYPE = 'AWS::RDS::DBInstance'
    def DBSubnetGroupName
    def MultiAZ
    def AvailabilityZone
    def DBInstanceClass
    def AllocatedStorage
    def Iops
    def Engine
    def EngineVersion
    def Port
    def DBParameterGroupName
    def DBName
    def DBSnapshotIdentifier
    def MasterUsername
    def MasterUserPassword
    def VPCSecurityGroups

    def DBInstance() {
    }

    def toResourceMap() {
        def map = [
            'Type'      : TYPE,
            'Properties': [
                'DBSubnetGroupName'   : DBSubnetGroupName,
                'MultiAZ'             : MultiAZ,
                'DBInstanceClass'     : DBInstanceClass,
                'AllocatedStorage'    : AllocatedStorage,
                'Engine'              : Engine,
                'EngineVersion'       : EngineVersion,
                'Port'                : Port,
                'DBParameterGroupName': DBParameterGroupName,
                'DBName'              : DBName,
                'MasterUsername'      : MasterUsername,
                'MasterUserPassword'  : MasterUserPassword,
                'VPCSecurityGroups'   : VPCSecurityGroups,
                'Tags'                : [
                    ['Key': 'Name', 'Value': id],
                    ['Key': 'Application', 'Value': ['Ref': 'AWS::StackId']]
                ]
            ]
        ]
        if (!this.MultiAZ) map['Properties']['AvailabilityZone'] = AvailabilityZone
        if (this.Iops) map['Properties']['Ipos'] = Iops
        if (this.DBSnapshotIdentifier) map['Properties']['DBSnapshotIdentifier'] = DBSnapshotIdentifier
        map
    }


}
