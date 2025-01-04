package com.luv2code.cruddemo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@Table(name="instructor_detail")
public class InstructorDetail {

    // annotate the class as an entity and map to db table

    // annotate the class with Lombok annotations

    // define the fields

    // annotate the fields with db column names

    // generate toString() method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="youtube_channel")
    private String youtubeChannel;

    @Column(name="hobby")
    private String hobby;

    // add @OneToOne annotation
    @OneToOne(mappedBy = "instructorDetail",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Instructor instructor;

    public InstructorDetail() {

    }

    public InstructorDetail(int id, String youtubeChannel, String hobby, Instructor instructor) {
        this.id = id;
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "InstructorDetail{" +
                "id=" + id +
                ", youtubeChannel='" + youtubeChannel + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}










