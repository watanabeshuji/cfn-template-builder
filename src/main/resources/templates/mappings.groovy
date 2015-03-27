package templates

cloudformation {
    mappings {
        "RegionMap" "us-northeast-1": ["AvailabilityZoneA": "ap-northeast-1a", "AvailabilityZoneC": "ap-northeast-1c"]
        "AddressMap" "IpRange": ["VPC": "10.0.0.0/24"]
    }
    resources {
        vpc id: "VPC", CidrBlock: "FindInMap:AddressMap:IpRange:VPC"
    }
}
