package com.michael.exercise.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.exception.NotFoundException;
import com.michael.exercise.models.User;
import com.michael.exercise.rowmappers.UserRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public User getUserByPhone(String phone) {
        String sql = """
            SELECT
                id, name, phone, email, password, role, created_at
            FROM users
            WHERE phone = ?;""";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, phone);
        } catch (EmptyResultDataAccessException ex) {
            String code = "not_found_user";
            String message = String.format("User not found for phone: %s", phone);
            throw new NotFoundException(code, message, ex);
        }
    }
}
