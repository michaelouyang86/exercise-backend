package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.TeacherExceptionAvailability;

@Component
public class TeacherExceptionAvailabilityRowMapper implements RowMapper<TeacherExceptionAvailability> {
    @Override
    public TeacherExceptionAvailability mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherExceptionAvailability exception = new TeacherExceptionAvailability();
        exception.setId(rs.getInt("id"));
        exception.setTeacherId(rs.getInt("teacher_id"));
        exception.setExceptionDate(rs.getDate("exception_date").toLocalDate());
        exception.setStartTime(rs.getTime("start_time").toLocalTime());
        exception.setEndTime(rs.getTime("end_time").toLocalTime());
        return exception;
    }
}
