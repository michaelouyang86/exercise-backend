package com.michael.exercise.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.rowmappers.TeacherRecurringAvailabilityRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TeacherRecurringAvailabilityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TeacherRecurringAvailabilityRowMapper teacherRecurringAvailabilityRowMapper;

    public void addTeacherRecurringAvailability(AddTeacherRecurringAvailability availability) {
        String sql = """
            INSERT INTO teacher_recurring_availabilities (
                teacher_id, day_of_week, start_time, end_time
            )
            VALUES (?, ?, ?, ?);""";
        jdbcTemplate.update(sql, availability.getTeacherId(), availability.getDayOfWeek().getValue(), availability.getStartTime(), availability.getEndTime());
    }

    public List<TeacherRecurringAvailability> listTeacherRecurringAvailabilities(int teacherId) {
        String sql = """
            SELECT
                id, teacher_id, day_of_week, start_time, end_time
            FROM teacher_recurring_availabilities
            WHERE teacher_id = ?
            ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), start_time;""";
        return jdbcTemplate.query(sql, teacherRecurringAvailabilityRowMapper, teacherId);
    }

    public List<TeacherRecurringAvailability> listTeacherRecurringAvailabilities(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                id, teacher_id, day_of_week, start_time, end_time
            FROM teacher_recurring_availabilities
            WHERE teacher_id = ?
            AND day_of_week = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, teacherRecurringAvailabilityRowMapper, teacherId, date.getDayOfWeek().name());
    }

    public void deleteTeacherRecurringAvailability(int teacherId, int recurringId) {
        String sql = """
            DELETE FROM teacher_recurring_availabilities
            WHERE teacher_id = ? AND id = ?;""";
        jdbcTemplate.update(sql, teacherId, recurringId);
    }
}
