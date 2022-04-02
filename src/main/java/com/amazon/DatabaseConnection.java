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
        String query = "insert into users (region, district, name, phone_number, status, profession, working_type, age, gender, education_level, chat_id) " +
                "values (?,?,?,?,?,?,?,?,?,?, ?)";
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
        preparedStatement.setString(11, user.getChatId());
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

    public List<User> getAllWorkers() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM users where status = false\n" +
                "ORDER BY id ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setRegion(resultSet.getString(2));
            user.setDistrict(resultSet.getString(3));
            user.setName(resultSet.getString(4));
            user.setPhoneNumber(resultSet.getString(5));
            user.setProfession(resultSet.getString(7));
            user.setWorkingType(resultSet.getString(8));
            user.setAge(resultSet.getString(9));
            user.setGender(resultSet.getString(10));
            user.setEducationLevel(resultSet.getString(11));
            userList.add(user);
        }
        return userList;
    }

    public List<User> getAllJobOwners() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM users where status = true\n" +
                "ORDER BY id ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setRegion(resultSet.getString(2));
            user.setDistrict(resultSet.getString(3));
            user.setName(resultSet.getString(4));
            user.setPhoneNumber(resultSet.getString(5));
            user.setProfession(resultSet.getString(7));
            user.setWorkingType(resultSet.getString(8));
            user.setAge(resultSet.getString(9));
            user.setGender(resultSet.getString(10));
            user.setEducationLevel(resultSet.getString(11));
            userList.add(user);
        }
        return userList;
    }

    public List<User> getAllUsers() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM users\n" +
                "ORDER BY id ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setRegion(resultSet.getString(2));
            user.setDistrict(resultSet.getString(3));
            user.setName(resultSet.getString(4));
            user.setPhoneNumber(resultSet.getString(5));
            user.setProfession(resultSet.getString(7));
            user.setWorkingType(resultSet.getString(8));
            user.setAge(resultSet.getString(9));
            user.setGender(resultSet.getString(10));
            user.setEducationLevel(resultSet.getString(11));
            user.setChatId(resultSet.getString(13));
            userList.add(user);
        }
        return userList;
    }

    public List<String> getAllUsersChatId() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "select distinct(chat_id) from users";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> userList = new ArrayList<>();
        while (resultSet.next()) {
            String chatId = resultSet.getString(1);
            userList.add(chatId);
        }
        return userList;
    }

    public Admin checkAdmin(String phoneNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "select * from admin where phone_number = '"+phoneNumber+"'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        Admin admin = new Admin();
        while (resultSet.next()) {
            admin.setName(resultSet.getString(2));
            admin.setPhoneNumber(resultSet.getString(3));
            admin.setPassword(resultSet.getString(4));
        }
        return admin;
    }

    public void savePhoto(Ads ads) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "insert into reklama (name, description, file_id)" +
                "values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, ads.getName());
        preparedStatement.setString(2, ads.getDescription());
        preparedStatement.setString(3, ads.getFileId());
        preparedStatement.execute();
    }

    public List<Ads> getAds() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM reklama\n" +
                "ORDER BY id ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Ads> adsList = new ArrayList<>();
        while (resultSet.next()) {
            Ads ads = new Ads();
            ads.setId(resultSet.getLong(1));
            ads.setDescription(resultSet.getString(2));
            ads.setFileId(resultSet.getString(3));
            ads.setName(resultSet.getString(4));
            adsList.add(ads);
        }
        return adsList;
    }

    public void deleteAd(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "delete from reklama where id = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
    }

    public Ads checkAd(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "select * from reklama where id = " + id;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        Ads ads = new Ads();
        while (resultSet.next()) {
            ads.setId(resultSet.getLong(1));
            ads.setDescription(resultSet.getString(2));
            ads.setFileId(resultSet.getString(3));
        }
        return ads;
    }

    public void saveProfession(String profession) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "insert into profession (name)" +
                "values (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, profession);
        preparedStatement.execute();
    }

    public List<String> getProfessions() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "SELECT * FROM profession\n" +
                "ORDER BY id ASC ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> professions = new ArrayList<>();
        while (resultSet.next()) {
            Profession profession = new Profession();
            profession.setId(resultSet.getLong(1));
            profession.setName(resultSet.getString(2));
            professions.add(profession.getName());
        }
        return professions;
    }

    public void deleteProfession(String name) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "delete from profession where name = '" + name + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
    }

    public Profession checkProfession(String name) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "select * from profession where name = '" + name + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        Profession profession = new Profession();
        while (resultSet.next()) {
            profession.setId(resultSet.getLong(1));
            profession.setName(resultSet.getString(2));
        }
        return profession;
    }

    public void saveAdmin(Admin admin) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "insert into admin (name, phone_number, password)" +
                "values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, admin.getName());
        preparedStatement.setString(2, admin.getPhoneNumber());
        preparedStatement.setString(3, admin.getPassword());
        preparedStatement.execute();
    }

    public void deleteAdmin(String phoneNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        String query = "delete from admin where phone_number = '" + phoneNumber + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
    }
}
