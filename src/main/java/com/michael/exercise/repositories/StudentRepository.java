package com.michael.exercise.repositories;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.dtos.CreateStudentRequest;
import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.exception.NotFoundException;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Student;
import com.michael.exercise.rowmappers.ScheduledClassRowMapper;
import com.michael.exercise.rowmappers.StudentRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StudentRepository {

    private static final String STUDENT = "STUDENT";
    private final JdbcTemplate jdbcTemplate;
    private final StudentRowMapper studentRowMapper;
    private final ScheduledClassRowMapper scheduledClassRowMapper;

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

    public void createStudent(CreateStudentRequest request, String password) {
        String sql = """
            INSERT INTO users (
                name, phone, email, password, role
            )
            VALUES (?, ?, ?, ?, ?);""";

        jdbcTemplate.update(sql, request.getName(), request.getPhone(),
            request.getEmail(), password, STUDENT);
    }

    public void deleteStudent(int id) {
        String sql = """
            DELETE FROM users
            WHERE id = ? AND role = ?;""";
        jdbcTemplate.update(sql, id, STUDENT);
    }

    public void scheduleClass(int studentId, ScheduleClassRequest request) {
        String sql = """
            INSERT INTO scheduled_classes (
                student_id, teacher_id, class_date, start_time
            )
            VALUES (?, ?, ?, ?);""";

        jdbcTemplate.update(sql, studentId, request.getTeacherId(),
            request.getClassDate(), request.getStartTime());
    }

    public List<ScheduledClass> getScheduledClasses(int studentId) {
        String sql = """
            SELECT
                id, student_id, teacher_id, class_date, start_time, created_at
            FROM scheduled_classes
            WHERE student_id = ?
            AND class_date >= CURRENT_DATE
            ORDER BY class_date, start_time;""";
        return jdbcTemplate.query(sql, scheduledClassRowMapper, studentId);
    }

    public void cancelScheduledClass(int studentId, int scheduledClassId) {
        String sql = """
            DELETE FROM scheduled_classes
            WHERE student_id = ? AND id = ?;""";
        jdbcTemplate.update(sql, studentId, scheduledClassId);
    }
}
