package com.example.odyssea.daos.userAuth;

import com.example.odyssea.entities.userAuth.User;
import com.example.odyssea.exceptions.DatabaseException;
import com.example.odyssea.exceptions.UserNotFoundException;
import com.example.odyssea.exceptions.UsernameNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, _) -> new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("first_name"),
            rs.getString("last_name")
    );

    public List<User> findAll(){
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with id : " + id + " doesn't exist."));
    }

    public void save(User user) {
        String sql = "INSERT INTO user (email, password, role, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole(), user.getFirstName(), user.getLastName());
        if(rowsAffected <= 0){
            throw new DatabaseException("Could not save user.");
        }
    }

    public void update(int id, String email, String firstname, String lastname) {
        if (!userExistsById(id)) {
            throw new UserNotFoundException("User with id : " + id + " doesn't exist.");
        }

        User existingUser = this.findById(id);

        String sql = "UPDATE user SET email = ?, first_name = ?, last_name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(
                sql,
                email != null ? email : existingUser.getEmail(),
                firstname != null ? firstname : existingUser.getFirstName(),
                lastname != null ? lastname : existingUser.getLastName(),
                id
        );

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update user with id : " + id);
        }
    }

    public void updatePassword(int id, String newPassword) {
        if (!userExistsById(id)) {
            throw new UserNotFoundException("User with id : " + id + " doesn't exist.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newPassword);

        String sql = "UPDATE user SET password = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, hashedPassword, id);

        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to update password for user with id : " + id);
        }
    }


    public void delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected <= 0) {
            throw new DatabaseException("Failed to delete account for user with id : " + id);
        }
    }


    public boolean userExistsById(int id) {
        String checkSql = "SELECT COUNT(*) FROM user WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        return count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, email) > 0;
    }

}
