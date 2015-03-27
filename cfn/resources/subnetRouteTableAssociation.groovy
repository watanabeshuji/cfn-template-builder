
resources {
    subnetRouteTableAssociation id: "SubnetRouteTableAssociation", SubnetId: [Ref: 'Subnet'], RouteTableId: [Ref: 'RouteTable']
}