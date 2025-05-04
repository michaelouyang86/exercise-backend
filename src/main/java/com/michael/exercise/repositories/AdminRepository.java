package com.michael.exercise.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.exception.NotFoundException;
import com.michael.exercise.models.Admin;
import com.michael.exercise.rowmappers.AdminRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AdminRowMapper adminRowMapper;

    // For authentication
    public Admin getAdminByUsername(String username) {
        String sql = """
            SELECT
                id, username, password
            FROM admins
            WHERE username = ?;""";
        try {
            return jdbcTemplate.queryForObject(sql, adminRowMapper, username);
        } catch (EmptyResultDataAccessException ex) {
            String code = "not_found_admin";
            String message = String.format("Admin not found for username: %s", username);
            throw new NotFoundException(code, message, ex);
        }
    }
}
