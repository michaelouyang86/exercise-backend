package com.michael.exercise.repositories;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.models.StudentPointsRecord;
import com.michael.exercise.rowmappers.StudentPointsRecordRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StudentPointRecordRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StudentPointsRecordRowMapper studentPointsRecordRowMapper;

    public void addStudentPointRecord(int studentId, int adjustedPoints, String reason, int pointsAfterAdjustment) {
        String sql = """
            INSERT INTO student_points_records (
                student_id, adjusted_points, reason, points_after_adjustment
            )
            VALUES (?, ?, ?, ?);""";
        jdbcTemplate.update(sql, studentId, adjustedPoints, reason, pointsAfterAdjustment);
    }

    public List<StudentPointsRecord> listStudentPointsRecords(int studentId) {
        String sql = """
            SELECT
                id, student_id, adjusted_points, reason, points_after_adjustment
            FROM student_points_records
            WHERE student_id = ?
            ORDER BY created_at DESC;""";
        return jdbcTemplate.query(sql, studentPointsRecordRowMapper, studentId);
    }
}
