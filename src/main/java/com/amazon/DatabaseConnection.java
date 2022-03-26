package com.amazon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabaseConnection {
    String url = "jdbc:postgresql://localhost:5432/jobmarket";
    String username = "postgres";
    String password = "123";

    public void saveUser(User user) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "insert into users (region, district, name, phone_number, status, profession, working_type, age, gender, education_level) " +
                "values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getRegion());
        preparedStatement.setString(2, user.getDistrict());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getPhoneNumber());
        preparedStatement.setBoolean(5, user.isStatus());
        preparedStatement.setString(6, user.getProfession());
        preparedStatement.setString(7, user.getWorkingType());
        preparedStatement.setString(8, user.getAge());
        preparedStatement.setString(9, user.getGender());
        preparedStatement.setString(10, user.getEducationLevel());
        preparedStatement.execute();
    }

    public List<User> getUser(User user) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "select * from users u where u.status = false and u.district = '" + user.getDistrict()
                + "' and u.profession = '" + user.getProfession() + "' and u.gender = '"
                + user.getGender() + "' and u.age = '" + user.getAge() + "' order by created_date desc;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        List<User> users = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            User user1 = new User();
            user1.setRegion(resultSet.getString(2));
            user1.setDistrict(resultSet.getString(3));
            user1.setName(resultSet.getString(4));
            user1.setPhoneNumber(resultSet.getString(5));
            user1.setStatus(resultSet.getBoolean(6));
            user1.setProfession(resultSet.getString(7));
            user1.setWorkingType(resultSet.getString(8));
            user1.setAge(resultSet.getString(9));
            user1.setGender(resultSet.getString(10));
            user1.setEducationLevel(resultSet.getString(11));
            users.add(user1);
        }
        return users;
    }
}
