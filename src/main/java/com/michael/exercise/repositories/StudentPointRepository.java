package com.michael.exercise.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StudentPointRepository {

    private final JdbcTemplate jdbcTemplate;

    public void createStudentPoint(int studentId, int points) {
        String sql = """
            INSERT INTO student_points (
                student_id, points
            )
            VALUES (?, ?);""";
        jdbcTemplate.update(sql, studentId, points);
    }

    public int getStudentPointsForUpdate(int studentId) {
        String sql = """
            SELECT
                points
            FROM student_points
            WHERE student_id = ?
            FOR UPDATE;""";
        return jdbcTemplate.queryForObject(sql, Integer.class, studentId);
    }

    public void setStudentPoints(int studentId, int newPoints) {
        String sql = """
            UPDATE student_points
            SET points = ?
            WHERE student_id = ?;""";
        jdbcTemplate.update(sql, newPoints, studentId);
    }
}
