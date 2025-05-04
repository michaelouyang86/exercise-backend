package com.michael.exercise.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.rowmappers.TeacherUnavailableDateRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TeacherUnavailableDateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherUnavailableDateRowMapper teacherUnavailableDateRowMapper;

    public void addTeacherUnavailableDate(int teacherId, AddTeacherUnavailableDateRequest request) {
        String sql = """
            INSERT INTO teacher_unavailable_dates (
                teacher_id, unavailable_date
            )
            VALUES (?, ?);""";
        jdbcTemplate.update(sql, teacherId, request.getUnavailableDate());
    }

    public boolean isUnavailable(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                COUNT(*)
            FROM teacher_unavailable_dates
            WHERE teacher_id = ? AND unavailable_date = ?;""";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, teacherId, date);
        return count > 0;
    }

    public List<TeacherUnavailableDate> listUpcomingTeacherUnavailableDates(int teacherId) {
        String sql = """
            SELECT
                id, teacher_id, unavailable_date
            FROM teacher_unavailable_dates
            WHERE teacher_id = ?
            AND unavailable_date >= CURRENT_DATE
            ORDER BY unavailable_date;""";
        return jdbcTemplate.query(sql, teacherUnavailableDateRowMapper, teacherId);
    }

    public void deleteTeacherUnavailableDate(int teacherId, int unavailableDateId) {
        String sql = """
            DELETE FROM teacher_unavailable_dates
            WHERE teacher_id = ? AND id = ?;""";
        jdbcTemplate.update(sql, teacherId, unavailableDateId);
    }
}
