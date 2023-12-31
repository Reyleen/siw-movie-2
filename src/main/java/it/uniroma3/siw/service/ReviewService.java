package it.uniroma3.siw.service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CredentialsService credentialsService;

    @Transactional
    public Review getReviewById(Long id) {
        return this.reviewRepository.findById(id).get();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        try {
            Review review = this.getReviewById(reviewId);
            Movie movie = review.getMovie();
            review.getUser().getReviews().remove(review);
            review.getMovie().getReviews().remove(review);
            review.getUser().getMoviesReviewed().remove(movie);
            this.movieRepository.save(movie);
            this.reviewRepository.delete(review);
        } catch (Exception e) {
            return;
        }
    }

    @Transactional
    public Review newReview(@Valid Review review, Long movieId) {
        Movie movie = this.movieRepository.findById(movieId).get();
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(user.getUsername());
        review.setMovie(movie);
        review.setUser(credentials.getUser());
        credentials.getUser().getMoviesReviewed().add(movie);
        this.reviewRepository.save(review);
        credentials.getUser().getReviews().add(review);
        movie.getReviews().add(review);
        this.movieRepository.save(movie);
        return review;
    }

}
