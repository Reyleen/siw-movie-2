package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class MovieController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieValidator movieValidator;

    @Autowired
    private  ReviewValidator reviewValidator;

    @Autowired
    private MovieService movieService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping(value="/admin/formNewMovie")
    public String formNewMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/formNewMovie.html";
    }

    @GetMapping(value="/admin/formUpdateMovie/{id}")
    public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).get());
        return "admin/formUpdateMovie.html";
    }

    @GetMapping(value="/admin/indexMovie")
    public String indexMovie() {
        return "admin/indexMovie.html";
    }

    @GetMapping(value="/admin/manageMovies")
    public String manageMovies(Model model) {
        model.addAttribute("movies", this.movieRepository.findAll());
        return "admin/manageMovies.html";
    }

    @GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
    public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {

        Artist director = this.artistRepository.findById(directorId).get();
        Movie movie = this.movieRepository.findById(movieId).get();
        movie.setDirector(director);
        this.movieRepository.save(movie);

        model.addAttribute("movie", movie);
        return "admin/formUpdateMovie.html";
    }


    @GetMapping(value="/admin/addDirector/{id}")
    public String addDirector(@PathVariable("id") Long id, Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("movie", movieRepository.findById(id).get());
        return "admin/directorsToAdd.html";
    }

    @PostMapping("/admin/movie")
    public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {

        this.movieValidator.validate(movie, bindingResult);
        if (!bindingResult.hasErrors()) {
            this.movieRepository.save(movie);
            model.addAttribute("movie", movie);
            return "movie.html";
        } else {
            return "admin/formNewMovie.html";
        }
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", this.movieRepository.findById(id).get());
        return "movie.html";
    }

    @GetMapping("/movie")
    public String getMovies(Model model) {
		/*
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
*/
        model.addAttribute("movies", this.movieRepository.findAll());
        //model.addAttribute("user", credentials.getUser());
        return "movies.html";
    }

    @GetMapping("/formSearchMovies")
    public String formSearchMovies() {
        return "formSearchMovies.html";
    }

    @PostMapping("/searchMovies")
    public String searchMovies(Model model, @RequestParam int year) {
        model.addAttribute("movies", this.movieRepository.findByYear(year));
        return "foundMovies.html";
    }

    @GetMapping("/admin/updateActors/{id}")
    public String updateActors(@PathVariable("id") Long id, Model model) {

        List<Artist> actorsToAdd = this.actorsToAdd(id);
        model.addAttribute("actorsToAdd", actorsToAdd);
        model.addAttribute("movie", this.movieRepository.findById(id).get());

        return "admin/actorsToAdd.html";
    }

    @GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
    public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Artist actor = this.artistRepository.findById(actorId).get();
        Set<Artist> actors = movie.getActors();
        actors.add(actor);
        this.movieRepository.save(movie);

        List<Artist> actorsToAdd = actorsToAdd(movieId);

        model.addAttribute("movie", movie);
        model.addAttribute("actorsToAdd", actorsToAdd);

        return "admin/actorsToAdd.html";
    }

    @GetMapping("/addReviewToMovie/{movieId}")
    public String addReviewToMovie(@PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        model.addAttribute("movie", movie);
        model.addAttribute("review", new Review());
        return "formNewReview.html";
    }

    @PostMapping("/review/{movieId}")
    public String newReview(@Valid @ModelAttribute("review") Review review, @PathVariable("movieId") Long movieId, BindingResult bindingResult, Model model) {
        this.reviewValidator.validate(review, bindingResult);
        Review newReview = this.reviewService.newReview(review, movieId);
        if (!bindingResult.hasErrors()) {
            Movie movie = this.movieService.addReviewToMovie(movieId, newReview.getId());
            model.addAttribute(newReview);
            model.addAttribute(movie);
            return "movie.html";
        } else {
            model.addAttribute("movie", this.movieService.getMovieById(movieId));
            return "formNewReview.html";
        }
    }

    @GetMapping("/admin/updateReviews/{movieId}")
    public String updateReviews(@PathVariable("movieId") Long movieId, Model model) {

        List<Review> reviewsToAdd = this.reviewsToAdd(movieId);
        model.addAttribute("reviewsToAdd", reviewsToAdd);
        model.addAttribute("movie", this.movieRepository.findById(movieId).get());

        return "admin/reviewsToAdd.html";
    }

    @GetMapping("/admin/removeReviewFromMovie/{reviewId}/{movieId}")
    public String deleteReview(@PathVariable("reviewId")Long reviewId, @PathVariable("movieId") Long movieId,Model model) {
        this.reviewService.deleteReview(reviewId);
        Movie movie = this.movieService.getMovieById(movieId);
        model.addAttribute("movie", movie );
        model.addAttribute("reviews", movie.getReviews());
        return "admin/reviewsToAdd.html";
    }

    @GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
    public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
        Movie movie = this.movieRepository.findById(movieId).get();
        Artist actor = this.artistRepository.findById(actorId).get();
        Set<Artist> actors = movie.getActors();
        actors.remove(actor);
        this.movieRepository.save(movie);

        List<Artist> actorsToAdd = actorsToAdd(movieId);

        model.addAttribute("movie", movie);
        model.addAttribute("actorsToAdd", actorsToAdd);

        return "admin/actorsToAdd.html";
    }

    private List<Artist> actorsToAdd(Long movieId) {
        List<Artist> actorsToAdd = new ArrayList<>();

        for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
            actorsToAdd.add(a);
        }
        return actorsToAdd;
    }

    private List<Review> reviewsToAdd(Long movieId) {
        List<Review> reviewsToAdd = new ArrayList<>();
        Movie movie = this.movieRepository.findById(movieId).get();
        for (Review review : reviewRepository.findByMovie(movie)){
            reviewsToAdd.add(review);
        }
        return reviewsToAdd;
    }
}
