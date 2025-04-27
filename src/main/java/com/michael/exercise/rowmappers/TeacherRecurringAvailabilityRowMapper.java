package com.michael.exercise.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.michael.exercise.models.DayOfWeekEnum;
import com.michael.exercise.models.TeacherRecurringAvailability;

@Component
public class TeacherRecurringAvailabilityRowMapper implements RowMapper<TeacherRecurringAvailability> {
    @Override
    public TeacherRecurringAvailability mapRow(ResultSet rs, int rowNum) throws SQLException {
        TeacherRecurringAvailability recurring = new TeacherRecurringAvailability();
        recurring.setId(rs.getInt("id"));
        recurring.setTeacherId(rs.getInt("teacher_id"));
        recurring.setDayOfWeek(DayOfWeekEnum.fromValue(rs.getString("day_of_week")));
        recurring.setStartTime(rs.getTime("start_time").toLocalTime());
        recurring.setEndTime(rs.getTime("end_time").toLocalTime());
        return recurring;
    }
}
