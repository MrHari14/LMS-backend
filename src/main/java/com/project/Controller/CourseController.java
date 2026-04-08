package com.project.Controller;

import com.project.entity.Course;
import com.project.entity.Course.CourseStatus;
import com.project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin( origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCoursesByInstructor(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructor(instructorId);
    }

    @GetMapping("/status/{status}")
    public List<Course> getCoursesByStatus(@PathVariable CourseStatus status) {
        return courseService.getCoursesByStatus(status);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        Optional<Course> courseOpt = courseService.getCourseById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setTitle(courseDetails.getTitle());
            course.setInstructor(courseDetails.getInstructor());
            course.setStatus(courseDetails.getStatus());
            return ResponseEntity.ok(courseService.updateCourse(course));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/approve")
    public ResponseEntity<Void> approveCourse(@PathVariable Long courseId) {
        courseService.approveCourse(courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Void> enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        courseService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok().build();
    }
}