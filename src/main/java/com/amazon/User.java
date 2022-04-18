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
    private String chatId;
    private String experience;

    @Override
    public String toString() {
        return "viloyat: " + this.region + "\n"
                +"tuman: " + this.district + "\n"
                +"ismi: " + this.name + "\n"
                +"tel raqami: " + this.phoneNumber + "\n"
                +"kasbi: " + this.profession + "\n"
                +"ish turi: " + this.workingType + "\n"
                +"yoshi: " + this.age + "\n"
                +"jinsi: " + this.gender + "\n"
                +"malumoti: " + this.educationLevel + "\n"
                +"tajribasi: " + this.experience;
    }
}
