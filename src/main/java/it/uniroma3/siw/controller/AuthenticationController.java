package it.uniroma3.siw.controller;

import javax.validation.Valid;

import it.uniroma3.siw.controller.validator.CredentialValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private CredentialValidator credentialsValidator;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(value = "/register")
    public String showRegisterForm (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "formRegisterUser";
    }

    @GetMapping(value = "/login")
    public String showLoginForm (Model model) {
        return "formLogin";
    }

    @GetMapping(value = "/")
    public String index(Model model) {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        model.addAttribute("images", images);
        model.addAttribute("movies", movieRepository.findAll());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "index.html";
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "admin/indexAdmin.html";
            }
        }
        return "index.html";
    }

    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        String[] images = new String[movies.size()];
        for(int i = 0; i < movies.size(); i++) {
            images[i] = java.util.Base64.getEncoder().encodeToString(movies.get(i).getImage());
        }
        model.addAttribute("images", images);
        model.addAttribute("movies", movieRepository.findAll());
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "index.html";
    }

    @PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult, @Valid
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {

        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "registerUser";
    }
}
