package com.sunrise.dental.dao;

import com.sunrise.dental.database.DatabaseConnection;
import com.sunrise.dental.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User login(String username, String password) {

        User user = null;

        String sql = """
                SELECT user_id, username, role
                FROM users
                WHERE username = ? AND password = ?
                """;

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                user = new User();

                user.setUserId(
                        resultSet.getInt("user_id")
                );

                user.setUsername(
                        resultSet.getString("username")
                );

                user.setRole(
                        resultSet.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}