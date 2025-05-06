package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.StudentPointsRecord;

@Component
public class StudentPointsRecordRowMapper implements RowMapper<StudentPointsRecord> {
    @Override
    public StudentPointsRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        StudentPointsRecord studentPointsRecord = new StudentPointsRecord();
        studentPointsRecord.setId(rs.getInt("id"));
        studentPointsRecord.setStudentId(rs.getInt("student_id"));
        studentPointsRecord.setAdjustedPoints(rs.getInt("adjusted_points"));
        studentPointsRecord.setReason(rs.getString("reason"));
        studentPointsRecord.setPointsAfterAdjustment(rs.getInt("points_after_adjustment"));
        return studentPointsRecord;
    }
}
