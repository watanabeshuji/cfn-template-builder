cloudformation {
    description "a cloudformation template."
    mappings {
        "AddressMap" "IpRange": ["VPC": "10.0.0.0/24"]
    }
    resources {
        vpc id: "VPC", CidrBlock: "FindInMap:AddressMap:IpRange:VPC",
            Tags: [Name: "dev-vpc", Enviroment: "development"]
    }
}