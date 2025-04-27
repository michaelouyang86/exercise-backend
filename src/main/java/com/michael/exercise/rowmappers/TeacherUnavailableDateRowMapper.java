package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.TeacherUnavailableDate;

@Component
public class TeacherUnavailableDateRowMapper implements RowMapper<TeacherUnavailableDate> {
    @Override
    public TeacherUnavailableDate mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherUnavailableDate unavailableDate = new TeacherUnavailableDate();
        unavailableDate.setId(rs.getInt("id"));
        unavailableDate.setTeacherId(rs.getInt("teacher_id"));
        unavailableDate.setUnavailableDate(rs.getDate("unavailable_date").toLocalDate());
        return unavailableDate;
    }
}
