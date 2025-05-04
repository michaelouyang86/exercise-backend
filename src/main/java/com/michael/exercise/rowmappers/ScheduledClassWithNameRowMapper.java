package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.ScheduledClassWithName;

@Component
public class ScheduledClassWithNameRowMapper implements RowMapper<ScheduledClassWithName> {
    @Override
    public ScheduledClassWithName mapRow(ResultSet rs, int rowNum) throws SQLException {
        ScheduledClassWithName scheduledClassWithName = new ScheduledClassWithName();
        scheduledClassWithName.setId(rs.getInt("id"));
        scheduledClassWithName.setStudentId(rs.getInt("student_id"));
        scheduledClassWithName.setStudentName(rs.getString("student_name"));
        scheduledClassWithName.setTeacherId(rs.getInt("teacher_id"));
        scheduledClassWithName.setTeacherName(rs.getString("teacher_name"));
        scheduledClassWithName.setClassDate(rs.getDate("class_date").toLocalDate());
        scheduledClassWithName.setStartTime(rs.getTime("start_time").toLocalTime());
        return scheduledClassWithName;
    }
}
