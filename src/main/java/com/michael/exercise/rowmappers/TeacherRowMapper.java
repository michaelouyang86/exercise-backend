package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.Teacher;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {
    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setName(rs.getString("name"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setEmail(rs.getString("email"));
        teacher.setCreatedAt(rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC));
        return teacher;
    }
}
