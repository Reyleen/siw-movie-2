package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ReviewService reviewService;

  @Transactional
  public void createNewMovie(@Valid Movie movie, MultipartFile file) throws IOException {
      byte[] bytes = file.getBytes();
      movie.setImage(bytes);
       this.movieRepository.save(movie);
   }

   @Transactional
   public void updateMovie(@Valid Movie m1, @Valid Movie m2, MultipartFile file) throws IOException{
      if(m2.getTitle() != null)
                  m1.setTitle(m2.getTitle());
      if(m2.getYear() != null)
        m1.setYear(m2.getYear());
      if (file != null  && !file.isEmpty()) {
          byte[] bytes = file.getBytes();
          m1.setImage(bytes);
      }
        this.movieRepository.save(m1);
   }

    public Movie getMovieById(Long id) {
        return this.movieRepository.findById(id).get();
    }

    public Iterable<Movie> getMovies() {
        return this.movieRepository.findAll();
    }

    public List<Movie> getMoviesByTitle(String title) {
        return this.movieRepository.findByTitle(title);
    }

    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = this.getMovieById(movieId);
        Set<Artist> actors = movie.getActors();
        for (Artist actor : actors) {
            actor.getActorOf().remove(movie);
        }
        Artist director = movie.getDirector();
        director.getDirectorOf().remove(movie);
        Set<Review> reviews = movie.getReviews();
        for (Review review : reviews) {
            review.setMovie(null);
        }
        this.movieRepository.delete(movie);
    }

    @Transactional
    public Movie addReviewToMovie(Long movieId, Long reviewId) {
        Movie movie = this.getMovieById(movieId);
        Review review = this.reviewService.getReviewById(reviewId);
        Set<Review> reviews = movie.getReviews();
        reviews.add(review);
        movie.setReviews(reviews);
        review.setMovie(movie);
        return this.movieRepository.save(movie);
    }

    @Transactional
    public List<Movie> getAllTheMovies() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }

    @Transactional
    public String[] getAllTheMovieImages(List<Movie> movies) {
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        return images;
    }

    @Transactional
    public void addDirectorToMovie(Artist director, Movie movie) {
        movie.setDirector(director);
        List<Movie> directors = director.getDirectorOf();
        directors.add(movie);
        this.artistRepository.save(director);
        this.movieRepository.save(movie);
    }

    @Transactional
    public List<Movie> getMoviesWithTitle(String title) {
      List <Movie> movies = new ArrayList<>();
        this.movieRepository.findByTitle(title).forEach(movies::add);
        return movies;
    }

    @Transactional
    public Movie addImages(Movie movie, MultipartFile multipartFile) throws IOException {
      byte[] image = multipartFile.getBytes();
        ArrayList<byte[]> images = movie.getImages();
        images.add(image);
        return this.movieRepository.save(movie);
    }

    @Transactional
    public String[] getImages(Movie movie) {
        String[] images = new String[movie.getImages().size()];
        for(int i = 0; i < movie.getImages().size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movie.getImages().get(i));
        }
        return images;
    }
}


