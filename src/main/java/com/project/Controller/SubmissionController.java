package com.project.Controller;

import com.project.entity.Submission;
import com.project.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin( origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> submission = submissionService.getSubmissionById(id);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/assignment/{assignmentId}")
    public List<Submission> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return submissionService.getSubmissionsByAssignment(assignmentId);
    }

    @PostMapping
    public Submission createSubmission(@RequestBody Submission submission) {
        return submissionService.createSubmission(submission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(@PathVariable Long id, @RequestBody Submission submissionDetails) {
        Optional<Submission> submissionOpt = submissionService.getSubmissionById(id);
        if (submissionOpt.isPresent()) {
            Submission submission = submissionOpt.get();
            submission.setStatus(submissionDetails.getStatus());
            submission.setGrade(submissionDetails.getGrade());
            return ResponseEntity.ok(submissionService.updateSubmission(submission));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<Void> gradeSubmission(@PathVariable Long submissionId, @RequestParam Integer grade) {
        submissionService.gradeSubmission(submissionId, grade);
        return ResponseEntity.ok().build();
    }
}