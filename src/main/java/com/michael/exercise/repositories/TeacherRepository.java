package com.michael.exercise.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.dtos.AddTeacherUnavailableDateRequest;
import com.michael.exercise.dtos.CreateTeacherRequest;
import com.michael.exercise.models.TeacherRecurringAvailability;
import com.michael.exercise.models.TeacherUnavailableDate;
import com.michael.exercise.models.AddTeacherExceptionAvailability;
import com.michael.exercise.models.AddTeacherRecurringAvailability;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.Teacher;
import com.michael.exercise.models.TeacherExceptionAvailability;
import com.michael.exercise.rowmappers.ScheduledClassRowMapper;
import com.michael.exercise.rowmappers.TeacherExceptionAvailabilityRowMapper;
import com.michael.exercise.rowmappers.TeacherRecurringAvailabilityRowMapper;
import com.michael.exercise.rowmappers.TeacherRowMapper;
import com.michael.exercise.rowmappers.TeacherUnavailableDateRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TeacherRepository {

    private static final String TEACHER = "TEACHER";
    private final JdbcTemplate jdbcTemplate;
    private final TeacherRowMapper teacherRowMapper;
    private final TeacherRecurringAvailabilityRowMapper teacherRecurringAvailabilityRowMapper;
    private final TeacherExceptionAvailabilityRowMapper teacherExceptionAvailabilityRowMapper;
    private final TeacherUnavailableDateRowMapper teacherUnavailableDateRowMapper;
    private final ScheduledClassRowMapper scheduledClassRowMapper;

    public List<Teacher> getTeachers() {
        String sql = """
            SELECT
                id, name, phone, email, created_at
            FROM users
            WHERE role = ?;""";
        return jdbcTemplate.query(sql, teacherRowMapper, TEACHER);
    }

    public void createTeacher(CreateTeacherRequest request, String password) {
        String sql = """
            INSERT INTO users (
                name, phone, email, password, role
            )
            VALUES (?, ?, ?, ?, ?);""";

        jdbcTemplate.update(sql, request.getName(), request.getPhone(),
            request.getEmail(), password, TEACHER);
    }

    public void deleteTeacher(int id) {
        String sql = "DELETE FROM users WHERE id = ? AND role = ?;";
        jdbcTemplate.update(sql, id, TEACHER);
    }

    public void addTeacherRecurringAvailability(AddTeacherRecurringAvailability availability) {
        String sql = """
            INSERT INTO teacher_recurring_availabilities (
                teacher_id, day_of_week, start_time, end_time
            )
            VALUES (?, ?, ?, ?);""";

        jdbcTemplate.update(sql, availability.getTeacherId(), availability.getDayOfWeek().getValue(), availability.getStartTime(), availability.getEndTime());
    }

    public List<TeacherRecurringAvailability> getTeacherRecurringAvailabilities(int teacherId) {
        String sql = """
            SELECT
                id, teacher_id, day_of_week, start_time, end_time
            FROM teacher_recurring_availabilities
            WHERE teacher_id = ?
            ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), start_time;""";
        return jdbcTemplate.query(sql, teacherRecurringAvailabilityRowMapper, teacherId);
    }

    public void deleteTeacherRecurringAvailability(int teacherId, int recurringId) {
        String sql = "DELETE FROM teacher_recurring_availabilities WHERE teacher_id = ? AND id = ?;";
        jdbcTemplate.update(sql, teacherId, recurringId);
    }

    public void addTeacherExceptionAvailability(AddTeacherExceptionAvailability exceptionAvailability) {
        String sql = """
            INSERT INTO teacher_exception_availabilities (
                teacher_id, exception_date, start_time, end_time
            )
            VALUES (?, ?, ?, ?);""";

        jdbcTemplate.update(sql, exceptionAvailability.getTeacherId(), exceptionAvailability.getExceptionDate(), exceptionAvailability.getStartTime(), exceptionAvailability.getEndTime());
    }

    public List<TeacherExceptionAvailability> getTeacherExceptionAvailabilities(int teacherId) {
        String sql = """
            SELECT
                id, teacher_id, exception_date, start_time, end_time
            FROM teacher_exception_availabilities
            WHERE teacher_id = ?
            AND exception_date >= CURRENT_DATE
            ORDER BY exception_date, start_time;""";
        return jdbcTemplate.query(sql, teacherExceptionAvailabilityRowMapper, teacherId);
    }

    public void deleteTeacherExceptionAvailability(int teacherId, int exceptionId) {
        String sql = "DELETE FROM teacher_exception_availabilities WHERE teacher_id = ? AND id = ?;";
        jdbcTemplate.update(sql, teacherId, exceptionId);
    }

    public void addTeacherUnavailableDate(int teacherId, AddTeacherUnavailableDateRequest request) {
        String sql = """
            INSERT INTO teacher_unavailable_dates (
                teacher_id, unavailable_date
            )
            VALUES (?, ?);""";

        jdbcTemplate.update(sql, teacherId, request.getUnavailableDate());
    }

    public List<TeacherUnavailableDate> getTeacherUnavailableDates(int teacherId) {
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
        String sql = "DELETE FROM teacher_unavailable_dates WHERE teacher_id = ? AND id = ?;";
        jdbcTemplate.update(sql, teacherId, unavailableDateId);
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

    public boolean hasExceptionAvailability(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                COUNT(*)
            FROM teacher_exception_availabilities
            WHERE teacher_id = ? AND exception_date = ?;""";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, teacherId, date);
        return count > 0;
    }

    public List<TeacherExceptionAvailability> getTeacherExceptionAvailabilities(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                id, teacher_id, exception_date, start_time, end_time
            FROM teacher_exception_availabilities
            WHERE teacher_id = ? AND exception_date = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, teacherExceptionAvailabilityRowMapper, teacherId, date);
    }

    public List<TeacherRecurringAvailability> getTeacherRecurringAvailabilities(int teacherId, LocalDate date) {
        String sql = """
            SELECT
                id, teacher_id, day_of_week, start_time, end_time
            FROM teacher_recurring_availabilities
            WHERE teacher_id = ? AND day_of_week = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, teacherRecurringAvailabilityRowMapper, teacherId, date.getDayOfWeek().name());
    }

    public List<ScheduledClass> getScheduledClasses(int teacherId, LocalDate classDate) {
        String sql = """
            SELECT
                id, student_id, teacher_id, class_date, start_time
            FROM scheduled_classes
            WHERE teacher_id = ? AND class_date = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, scheduledClassRowMapper, teacherId, classDate);
    }
}
