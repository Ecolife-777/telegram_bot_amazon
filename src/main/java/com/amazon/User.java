package com.amazon;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String region;
    private String district;
    private String name;
    private String phoneNumber;
    private boolean status; //true-employee, false-worker
    private String profession;
    private String workingType;
    private String age;
    private String gender;
    private String educationLevel;

    @Override
    public String toString() {
        return "region: " + this.region + "\n"
                +"district: " + this.district + "\n"
                +"name: " + this.name + "\n"
                +"phoneNumber: " + this.phoneNumber + "\n"
                +"status: " + this.status + "\n"
                +"profession: " + this.profession + "\n"
                +"workingType: " + this.workingType + "\n"
                +"age: " + this.age + "\n"
                +"gender: " + this.gender + "\n"
                +"educationLevel: " + this.educationLevel;
    }
}
