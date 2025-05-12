package com.michael.exercise.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.michael.exercise.dtos.ScheduleClassRequest;
import com.michael.exercise.models.ScheduledClass;
import com.michael.exercise.models.ScheduledClassWithName;
import com.michael.exercise.rowmappers.ScheduledClassRowMapper;
import com.michael.exercise.rowmappers.ScheduledClassWithNameRowMapper;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ScheduledClassRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ScheduledClassRowMapper scheduledClassRowMapper;
    private final ScheduledClassWithNameRowMapper scheduledClassWithNameRowMapper;

    public void scheduleClass(int studentId, ScheduleClassRequest request) {
        String sql = """
            INSERT INTO scheduled_classes (
                student_id, teacher_id, class_date, start_time
            )
            VALUES (?, ?, ?, ?);""";

        jdbcTemplate.update(sql, studentId, request.getTeacherId(),
            request.getClassDate(), request.getStartTime());
    }

    public ScheduledClassWithName getScheduledClass(int id) {
        String sql = """
            SELECT
                sc.id, sc.student_id, student_user.name AS student_name,
                sc.teacher_id, teacher_user.name AS teacher_name,
                sc.class_date, sc.start_time
            FROM scheduled_classes sc
            JOIN users student_user ON sc.student_id = student_user.id
            JOIN users teacher_user ON sc.teacher_id = teacher_user.id
            WHERE sc.id = ?;""";
        return jdbcTemplate.queryForObject(sql, scheduledClassWithNameRowMapper, id);
    }

    public List<ScheduledClassWithName> listStudentScheduledClasses(int studentId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT
                sc.id, sc.student_id, student_user.name AS student_name,
                sc.teacher_id, teacher_user.name AS teacher_name,
                sc.class_date, sc.start_time
            FROM scheduled_classes sc
            JOIN users student_user ON sc.student_id = student_user.id
            JOIN users teacher_user ON sc.teacher_id = teacher_user.id
            WHERE sc.student_id = ?
            AND sc.class_date >= ?
            AND sc.class_date <= ?
            ORDER BY sc.class_date, sc.start_time;""";
        return jdbcTemplate.query(sql, scheduledClassWithNameRowMapper, studentId, startDate, endDate);
    }

    public List<ScheduledClassWithName> listTeacherScheduledClasses(int teacherId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT
                sc.id, sc.student_id, student_user.name AS student_name,
                sc.teacher_id, teacher_user.name AS teacher_name,
                sc.class_date, sc.start_time
            FROM scheduled_classes sc
            JOIN users student_user ON sc.student_id = student_user.id
            JOIN users teacher_user ON sc.teacher_id = teacher_user.id
            WHERE sc.teacher_id = ?
            AND sc.class_date >= ?
            AND sc.class_date <= ?
            ORDER BY sc.class_date, sc.start_time;""";
        return jdbcTemplate.query(sql, scheduledClassWithNameRowMapper, teacherId, startDate, endDate);
    }

    public int cancelStudentClass(int studentId, int scheduledClassId) {
        String sql = """
            DELETE FROM scheduled_classes
            WHERE student_id = ? AND id = ?;""";
        return jdbcTemplate.update(sql, studentId, scheduledClassId);
    }

    public int cancelTeacherClass(int teacherId, int scheduledClassId) {
        String sql = """
            DELETE FROM scheduled_classes
            WHERE teacher_id = ? AND id = ?;""";
        return jdbcTemplate.update(sql, teacherId, scheduledClassId);
    }

    public List<ScheduledClass> listTeacherScheduledClasses(int teacherId, LocalDate classDate) {
        String sql = """
            SELECT
                id, student_id, teacher_id, class_date, start_time
            FROM scheduled_classes
            WHERE teacher_id = ? AND class_date = ?
            ORDER BY start_time;""";
        return jdbcTemplate.query(sql, scheduledClassRowMapper, teacherId, classDate);
    }
}
