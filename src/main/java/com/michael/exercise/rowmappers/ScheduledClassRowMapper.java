package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.ScheduledClass;

@Component
public class ScheduledClassRowMapper implements RowMapper<ScheduledClass> {
    @Override
    public ScheduledClass mapRow(ResultSet rs, int rowNum) throws SQLException {
        ScheduledClass scheduledClass = new ScheduledClass();
        scheduledClass.setId(rs.getInt("id"));
        scheduledClass.setStudentId(rs.getInt("student_id"));
        scheduledClass.setTeacherId(rs.getInt("teacher_id"));
        scheduledClass.setClassDate(rs.getDate("class_date").toLocalDate());
        scheduledClass.setStartTime(rs.getTime("start_time").toLocalTime());
        return scheduledClass;
    }
}
