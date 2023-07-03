package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Transactional
    public void createNewArtist(@Valid Artist artist, MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        artist.setImage(bytes);
        this.artistRepository.save(artist);
    }

    @Transactional
    public void updateArtist(@Valid Artist a1, @Valid Artist a2,MultipartFile file) throws IOException {
        if(a2.getSurname()!=null)
            a1.setSurname(a2.getSurname());
        if(a2.getName()!=null)
            a1.setName(a2.getName());
        if(a2.getDateOfBirth()!=null)
            a1.setDateOfBirth(a2.getDateOfBirth());
        if(a2.getDateOfDeath()!=null)
            a1.setDateOfDeath(a2.getDateOfDeath());
        if (file != null  && !file.isEmpty())
        {
            byte[] bytes = file.getBytes();
            a1.setImage(bytes);
        }
        this.artistRepository.save(a1);
    }

    @Transactional
    public Iterable<Artist> getAllArtists() {
        return this.artistRepository.findAll();
    }

    @Transactional
    public Artist getActorById(Long actorId) {
        return artistRepository.findById(actorId).get();
    }

    @Transactional
    public Iterable<Artist> findActorsNotInMovie(Long movieId) {
        return this.artistRepository.findActorsNotInMovie(movieId);
    }

    @Transactional
    public boolean existsByNameAndSurname(String name, String surname) {
        return artistRepository.existsByNameAndSurname(name, surname);
    }

    @Transactional
    public void addArtist(@Valid Artist artist) {
        this.artistRepository.save(artist);
    }

    @Transactional
    public void deleteArtist(Long artistId) {
        Artist artist = this.getActorById(artistId);
        Set<Movie> movies = artist.getActorOf();
        for(Movie movie : movies) {
            movie.getActors().remove(artist);
        }
        List<Movie> directedMovies = artist.getDirectorOf();
       for (Movie movie : directedMovies) {
            movie.setDirector(null);
        }
        this.artistRepository.delete(artist);
    }


}