package com.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String fileUrl;

    private String fileName;

    public enum Type {
        PPT, PDF, DOC, VIDEO
    }

    // Constructors
    public Material() {}

    public Material(Course course, String title, Type type, String fileUrl, String fileName) {
        this.course = course;
        this.title = title;
        this.type = type;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}
