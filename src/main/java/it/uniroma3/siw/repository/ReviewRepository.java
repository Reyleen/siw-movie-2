package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    public boolean existsByUserAndMovie(User user, Movie movie);

    public List<Review> findByMovie(Movie movie);

    public List<Review> findByOrderByRatingAsc();
}
