package com.project.service;

import com.project.entity.Submission;
import com.project.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public Optional<Submission> getSubmissionByAssignmentAndStudent(Long assignmentId, Long studentId) {
        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId);
    }

    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission updateSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public void deleteSubmission(Long id) {
        submissionRepository.deleteById(id);
    }

    public void gradeSubmission(Long submissionId, Integer grade) {
        Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);
        if (submissionOpt.isPresent()) {
            Submission submission = submissionOpt.get();
            submission.setGrade(grade);
            submission.setStatus(Submission.Status.GRADED);
            submissionRepository.save(submission);
        }
    }
}