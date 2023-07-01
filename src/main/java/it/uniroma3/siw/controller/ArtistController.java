package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistService artistService;

    @GetMapping(value="/admin/formNewArtist")
    public String formNewArtist(Model model) {
        model.addAttribute("artist", new Artist());
        return "admin/formNewArtist.html";
    }

    @GetMapping(value="/admin/indexArtist")
    public String indexArtist() {
        return "admin/indexArtist.html";
    }

    @GetMapping(value="/admin/manageArtists")
    public String manageArtist(Model model) {
        model.addAttribute("artists", this.artistRepository.findAll());
        return "admin/manageArtists.html";
    }

    @GetMapping("/admin/removeArtist/{artistId}")
    public String deleteArtist(@PathVariable("artistId")Long artistId, Model model) {
        this.artistService.deleteArtist(artistId);
        return "redirect:/admin/manageArtists";
    }

    @PostMapping("/admin/artist")
    public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
            this.artistService.createNewArtist(artist, multipartFile);
            byte[] photo = artist.getImage();
            if(photo != null) {
                String image = java.util.Base64.getEncoder().encodeToString(photo);
                model.addAttribute("image", image);
            }
            model.addAttribute("artist", artist);
            return "artist.html";
        } else {
            model.addAttribute("messaggioErrore", "Questo artista esiste già");
            return "admin/formNewArtist.html";
        }
    }

    @GetMapping("/artist/{id}")
    public String getArtist(@PathVariable("id") Long id, Model model) {
        Artist artist = this.artistService.getActorById(id);
        byte[] photo = artist.getImage();
        if(photo != null) {
            String image = java.util.Base64.getEncoder().encodeToString(photo);
            model.addAttribute("image", image);
        }
        model.addAttribute("artist", artist);
        return "artist.html";
    }

    @GetMapping("/artist")
    public String getArtists(Model model) {
        model.addAttribute("artists", this.artistRepository.findAll());
        return "artists.html";
    }
}
