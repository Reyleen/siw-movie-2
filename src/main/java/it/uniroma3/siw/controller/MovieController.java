package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.CredentialsService;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("admin/movie")
    public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult,  Model model,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {

        //this.movieValidator.validate(movie, bindingResult);
        if (movie.getTitle()!=null && movie.getYear()!=null
                && !movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())){
            this.movieService.createNewMovie(movie, multipartFile);
            byte[] photo = movie.getImage();
            if(photo != null) {
                String image = java.util.Base64.getEncoder().encodeToString(photo);
                model.addAttribute("image", image);
            }
            model.addAttribute("movie", movie);
            return "movie.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo film esiste già");
            return "admin/formNewMovie.html";
        }
    }

    @GetMapping(value="/admin/formMovieUpdate/{movieId}")
    public String manageMovie(@PathVariable("movieId") Long id ,Model model) {
        byte[] photo = movieRepository.findById(id).get().getImage();
        if(photo != null) {
            String image = java.util.Base64.getEncoder().encodeToString(photo);
            model.addAttribute("image", image);
        }
        model.addAttribute("movie", this.movieRepository.findById(id).get());
        model.addAttribute("movie2", new Movie());
        return "admin/formUpdateMovie2.html";
    }

    @PostMapping(value="/admin/formUpdateMovie/{id}")
    public String formMovieUpdate(@PathVariable("id") Long id, @Valid @ModelAttribute("movie2") Movie movie, BindingResult bindingResult, Model model,
                                  @RequestParam("image") MultipartFile multipartFile) throws IOException  {
        Movie toUpdate = this.movieRepository.findById(id).get();
        if (movie.getTitle()!=null && movie.getYear()!=null
                && !movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear())){
            this.movieService.updateMovie(toUpdate, movie, multipartFile);
            byte[] photo = toUpdate.getImage();
            if(photo != null) {
                String image = java.util.Base64.getEncoder().encodeToString(photo);
                model.addAttribute("image", image);
            }
            model.addAttribute("movie", toUpdate);
            return "admin/formUpdateMovie.html";

        } else {
            byte[] photo = movieRepository.findById(id).get().getImage();
            if(photo != null) {
                String image = java.util.Base64.getEncoder().encodeToString(photo);
                model.addAttribute("image", image);
            }
            model.addAttribute("messaggioErrore", "Questo film esiste già");
            model.addAttribute("movie", movieRepository.findById(id).get());
            return "admin/formUpdateMovie2.html";
        }
    }

    @GetMapping (value="/admin/formUpdateMovie/{id}")
    public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
        model.addAttribute("movie", movieRepository.findById(id).get());
        byte[] photo = movieRepository.findById(id).get().getImage();
        if(photo != null) {
            String image = java.util.Base64.getEncoder().encodeToString(photo);
            model.addAttribute("image", image);
        }
        return "admin/formUpdateMovie.html";
    }

    @GetMapping(value="/admin/indexMovie")
    public String indexMovie() {
        return "admin/indexMovie.html";
    }

    @GetMapping(value="/admin/manageMovies")
    public String manageMovies(Model model) {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        model.addAttribute("images", images);
        model.addAttribute("movies", this.movieRepository.findAll());
        return "admin/manageMovies.html";
    }

    @GetMapping("/admin/removeMovie/{movieId}")
    public String deleteMovie(@PathVariable("movieId")Long movieId, Model model) {
        this.movieService.deleteMovie(movieId);
        return "redirect:/admin/manageMovies";
    }

    @GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
    public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {

        Artist director = this.artistRepository.findById(directorId).get();
        Movie movie = this.movieRepository.findById(movieId).get();
        movie.setDirector(director);
        List<Movie> directors = director.getDirectorOf();
        directors.add(movie);
        this.artistRepository.save(director);
        this.movieRepository.save(movie);
        byte[] photo = movie.getImage();
        if(photo != null) {
            String image = java.util.Base64.getEncoder().encodeToString(photo);
            model.addAttribute("image", image);
        }
        model.addAttribute("movie", movie);
        return "admin/formUpdateMovie.html";
    }

    @GetMapping(value="/admin/addDirector/{id}")
    public String addDirector(@PathVariable("id") Long id, Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("movie", movieRepository.findById(id).get());
        return "admin/directorsToAdd.html";
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable("id") Long id, Model model) {
        Movie movie = this.movieRepository.findById(id).get();
        byte[] photo = movie.getImage();
        if(photo != null) {
            String image = java.util.Base64.getEncoder().encodeToString(photo);
            model.addAttribute("image", image);
        }
        model.addAttribute("movie", movie);
        return "movie.html";
    }

    @GetMapping("/movie")
    public String getMovies(Model model) {
		/*
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
*/
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        model.addAttribute("images", images);
        model.addAttribute("movies", this.movieRepository.findAll());
        //model.addAttribute("user", credentials.getUser());
        return "movies.html";
    }

    @GetMapping("/formSearchMovies")
    public String formSearchMovies() {
        return "formSearchMovies.html";
    }

    @PostMapping("/searchMovies")
    public String searchMovies(Model model, @RequestParam String title) {
        model.addAttribute("movies", this.movieRepository.findByTitle(title));
        List<Movie> movies = new ArrayList<>();
        movieRepository.findByTitle(title).forEach(movies::add);
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        model.addAttribute("images", images);
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
        Set<Movie> movies = actor.getActorOf();
        movies.add(movie);
        this.movieRepository.save(movie);
        this.artistRepository.save(actor);

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
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User user = credentials.getUser();
        Movie m = movieRepository.findById(movieId).get();
        Set<Movie>movies = user.getMoviesReviewed();
        if(!movies.contains(m)){
            Review newReview = this.reviewService.newReview(review, movieId);
            Movie movie = this.movieService.addReviewToMovie(movieId, newReview.getId());
            byte[] photo = movie.getImage();
            if(photo != null) {
                String image = java.util.Base64.getEncoder().encodeToString(photo);
                model.addAttribute("image", image);
            }
            model.addAttribute(newReview);
            model.addAttribute(movie);
            return "movie.html";
        } else {
            model.addAttribute("messaggioErrore", "Hai già scritto una recensione per questo film!");
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
        Set<Movie> movies = actor.getActorOf();
        movies.remove(movie);
        this.artistRepository.save(actor);
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
