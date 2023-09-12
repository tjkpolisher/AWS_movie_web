package com.example.demo.entity;

import javax.management.loading.PrivateClassLoader;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="movie_tbl")
public class Movie {
    @Id
    private int num;
    private String title;
    private String poster;
    private String degree;
    private String genre;

    @Column(name="open_date")
    private String openDate;
    
    private String country;

    @Column(name="movie_time")
    private String movieTime;

    private String synopsys;
}
