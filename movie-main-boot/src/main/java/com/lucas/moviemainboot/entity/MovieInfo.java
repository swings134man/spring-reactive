package com.lucas.moviemainboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {
    private String movieInfoId;
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate release_date;
}
