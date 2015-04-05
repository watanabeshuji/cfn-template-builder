resources {
    dbInstance id: "DbPrd", DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: true, DBInstanceClass: "db.m3.xlarge", AllocatedStorage: "200", Iops: 1000, Engine: "mysql", EngineVersion: "5.6.19", Port: "3306", DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234", VPCSecurityGroups: [[Ref: "Internal"]]
    dbInstance id: "DbDev", DBSubnetGroupName: [Ref: "DbSubnetGroup"], MultiAZ: false, AvailabilityZone: "ap-northeast-1a", DBInstanceClass: "db.m1.small", AllocatedStorage: "50", Engine: "mysql", EngineVersion: "5.6.19", Port: "3306", DBParameterGroupName: "default.mysql5.6", DBName: "app", MasterUsername: "root", MasterUserPassword: "pass1234", VPCSecurityGroups: [[Ref: "Internal"]], DBSnapshotIdentifier: "snapshot01"
}

