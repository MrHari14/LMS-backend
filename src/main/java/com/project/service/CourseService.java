package com.project.service;

import com.project.entity.Course;
import com.project.entity.Enrollment;
import com.project.entity.User;
import com.project.repository.CourseRepository;
import com.project.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

public List<Course> getCoursesByStatus(Course.CourseStatus status) {
        return courseRepository.findByStatus(status);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void approveCourse(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setStatus(Course.CourseStatus.PUBLISHED);
            courseRepository.save(course);
        }
    }

    public void enrollStudent(Long courseId, Long studentId) {
        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            Enrollment enrollment = new Enrollment(new User(studentId), new Course(courseId));
            enrollmentRepository.save(enrollment);
            // Update student count
            Optional<Course> courseOpt = courseRepository.findById(courseId);
            if (courseOpt.isPresent()) {
                Course course = courseOpt.get();
                course.setStudentCount(course.getStudentCount() + 1);
                courseRepository.save(course);
            }
        }
    }
}