package com.lucas.moviemainboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private MovieInfo movieInfo;
    private List<Review> reviewsList;
}
