package com.michael.exercise.services.common;

import java.util.List;

import org.springframework.stereotype.Service;

import com.michael.exercise.models.StudentPointsRecord;
import com.michael.exercise.repositories.StudentPointRecordRepository;
import com.michael.exercise.repositories.StudentPointRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentPointService {

    private final StudentPointRepository studentPointRepository;
    private final StudentPointRecordRepository studentPointRecordRepository;

    public void initializeStudentPoints(int studentId, int points) {
        studentPointRepository.createStudentPoint(studentId, points);
        if (points > 0) {
            String reason = String.format("購買課程: %d 堂", points);
            studentPointRecordRepository.addStudentPointRecord(studentId, points, reason, points);
        }
    }

    public int getStudentPoints(int studentId) {
        return studentPointRepository.getStudentPoints(studentId);
    }

    public int getStudentPointsForUpdate(int studentId) {
        return studentPointRepository.getStudentPointsForUpdate(studentId);
    }

    public List<StudentPointsRecord> listStudentPointsRecords(int studentId) {
        return studentPointRecordRepository.listStudentPointsRecords(studentId);
    }

    public void deductOnePointFromStudent(int studentId, int currentPoints, String reason) {
        // Set student points to the new value
        int adjustedPoints = -1;
        int pointsAfterAdjustment = currentPoints + adjustedPoints;
        studentPointRepository.setStudentPoints(studentId, pointsAfterAdjustment);
        // Create a record for the point deduction
        studentPointRecordRepository.addStudentPointRecord(studentId, adjustedPoints, reason, pointsAfterAdjustment);
    }

    public void refundOnePointToStudent(int studentId, int currentPoints, String reason) {
        // Set student points to the new value
        int adjustedPoints = 1;
        int pointsAfterAdjustment = currentPoints + adjustedPoints;
        studentPointRepository.setStudentPoints(studentId, pointsAfterAdjustment);
        // Create a record for the point refund
        studentPointRecordRepository.addStudentPointRecord(studentId, adjustedPoints, reason, pointsAfterAdjustment);
    }
}
