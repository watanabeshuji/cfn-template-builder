resources {
    dbSubnetGroup id: "DBSubnetGroup", DBSubnetGroupDescription: "DB subnet group", SubnetIds: ["Ref:PrivateA", "Ref:PrivateC"]
}
