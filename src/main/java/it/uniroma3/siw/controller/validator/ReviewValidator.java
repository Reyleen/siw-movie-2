package it.uniroma3.siw.controller.validator;


import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Set;

@Component
public class ReviewValidator implements Validator {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void validate(Object o,Errors errors) {
        /*Movie m = movieRepository.findById(id).get();
        if (m!=null && u!=null) {
            Set<Movie>movies = u.getMoviesReviewed();
            if(movies.contains(m))
                errors.reject("review.duplicate");
        }*/
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return Review.class.equals(aClass);
    }
}
