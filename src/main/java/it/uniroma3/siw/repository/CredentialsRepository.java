package it.uniroma3.siw.repository;

import java.util.Optional;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.User;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    public Optional<Credentials> findByUsername(String username);

    public boolean existsByUsername(String username);

}