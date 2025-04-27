package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.Student;

@Component
public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setPhone(rs.getString("phone"));
        student.setEmail(rs.getString("email"));
        student.setCreatedAt(rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC));
        return student;
    }
}
