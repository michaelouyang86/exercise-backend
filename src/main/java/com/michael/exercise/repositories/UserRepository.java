package com.michael.exercise.repositories;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.exception.NotFoundException;
import com.michael.exercise.models.Student;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherIdAndName;
import com.michael.exercise.models.User;
import com.michael.exercise.rowmappers.StudentRowMapper;
import com.michael.exercise.rowmappers.TeacherIdAndNameRowMapper;
import com.michael.exercise.rowmappers.TeacherRowMapper;
import com.michael.exercise.rowmappers.UserRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepository {

    private static final String STUDENT = "STUDENT";
    private static final String TEACHER = "TEACHER";

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final StudentRowMapper studentRowMapper;
    private final TeacherRowMapper teacherRowMapper;
    private final TeacherIdAndNameRowMapper teacherIdAndNameRowMapper;

    // For authentication
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

    public List<TeacherIdAndName> listTeachersIdAndName() {
        String sql = """
            SELECT
                id, name
            FROM users
            WHERE role = ?;""";
        return jdbcTemplate.query(sql, teacherIdAndNameRowMapper, TEACHER);
    }

    public int createStudent(CreateStudentRequest request, String encodedPassword) {
        String sql = """
            INSERT INTO users (
                name, phone, email, password, role
            )
            VALUES (?, ?, ?, ?, ?);""";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, request.getName());
            ps.setString(2, request.getPhone());
            ps.setString(3, request.getEmail());
            ps.setString(4, encodedPassword);
            ps.setString(5, STUDENT);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Student getStudentByPhone(String phone) {
        String sql = """
            SELECT
                id, name, phone, email, created_at
            FROM users
            WHERE phone = ?
            AND role = ?;""";
        try {
            return jdbcTemplate.queryForObject(sql, studentRowMapper, phone, STUDENT);
        } catch (EmptyResultDataAccessException ex) {
            String code = "not_found_student";
            String message = String.format("Student not found for phone: %s", phone);
            throw new NotFoundException(code, message, ex);
        }
    }

    public void deleteStudent(int id) {
        String sql = """
            DELETE FROM users
            WHERE id = ? AND role = ?;""";
        jdbcTemplate.update(sql, id, STUDENT);
    }

    public void createTeacher(CreateTeacherRequest request, String encodedPassword) {
        String sql = """
            INSERT INTO users (
                name, phone, email, password, role
            )
            VALUES (?, ?, ?, ?, ?);""";

        jdbcTemplate.update(sql, request.getName(), request.getPhone(),
            request.getEmail(), encodedPassword, TEACHER);
    }

    public List<Teacher> listTeachers() {
        String sql = """
            SELECT
                id, name, phone, email, created_at
            FROM users
            WHERE role = ?;""";
        return jdbcTemplate.query(sql, teacherRowMapper, TEACHER);
    }

    public void deleteTeacher(int id) {
        String sql = """
            DELETE FROM users
            WHERE id = ? AND role = ?;""";
        jdbcTemplate.update(sql, id, TEACHER);
    }
}
