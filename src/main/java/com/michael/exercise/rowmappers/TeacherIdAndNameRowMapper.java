package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.TeacherIdAndName;

@Component
public class TeacherIdAndNameRowMapper implements RowMapper<TeacherIdAndName> {
    @Override
    public TeacherIdAndName mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherIdAndName teacherIdAndName = new TeacherIdAndName();
        teacherIdAndName.setId(rs.getInt("id"));
        teacherIdAndName.setName(rs.getString("name"));
        return teacherIdAndName;
    }
}
