package com.michael.exercise.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StudentPointRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public void addStudentPointRecord(int studentId, int adjustedPoints, String reason, int pointsAfterAdjustment) {
        String sql = """
            INSERT INTO student_points_records (
                student_id, adjusted_points, reason, points_after_adjustment
            )
            VALUES (?, ?, ?, ?);""";
        jdbcTemplate.update(sql, studentId, adjustedPoints, reason, pointsAfterAdjustment);
    }
}
