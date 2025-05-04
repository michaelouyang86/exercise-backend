package com.michael.exercise.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.rowmappers.TeacherExceptionAvailabilityRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TeacherExceptionAvailabilityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherExceptionAvailabilityRowMapper teacherExceptionAvailabilityRowMapper;

    public void addTeacherExceptionAvailability(AddTeacherExceptionAvailability exceptionAvailability) {
        String sql = """
            INSERT INTO teacher_exception_availabilities (
                teacher_id, exception_date, start_time, end_time
            )
            VALUES (?, ?, ?, ?);""";
        jdbcTemplate.update(sql, exceptionAvailability.getTeacherId(), exceptionAvailability.getExceptionDate(), exceptionAvailability.getStartTime(), exceptionAvailability.getEndTime());
    }

    public boolean hasExceptionAvailability(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                COUNT(*)
            FROM teacher_exception_availabilities
            WHERE teacher_id = ? AND exception_date = ?;""";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, teacherId, date);
        return count > 0;
    }

    public List<TeacherExceptionAvailability> listUpcomingTeacherExceptionAvailabilities(int teacherId) {
        String sql = """
            SELECT
                id, teacher_id, exception_date, start_time, end_time
            FROM teacher_exception_availabilities
            WHERE teacher_id = ?
            AND exception_date >= CURRENT_DATE
            ORDER BY exception_date, start_time;""";
        return jdbcTemplate.query(sql, teacherExceptionAvailabilityRowMapper, teacherId);
    }

    public List<TeacherExceptionAvailability> listTeacherExceptionAvailabilities(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                id, teacher_id, exception_date, start_time, end_time
            FROM teacher_exception_availabilities
            WHERE teacher_id = ?
            AND exception_date = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, teacherExceptionAvailabilityRowMapper, teacherId, date);
    }

    public void deleteTeacherExceptionAvailability(int teacherId, int exceptionId) {
        String sql = """
            DELETE FROM teacher_exception_availabilities
            WHERE teacher_id = ? AND id = ?;""";
        jdbcTemplate.update(sql, teacherId, exceptionId);
    }
}
