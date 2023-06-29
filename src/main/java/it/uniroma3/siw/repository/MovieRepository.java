package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

    public List<Movie> findByYear(Integer year);
    public boolean existsByTitleAndYear(String title, Integer year);
    public List<Movie> findByOrderByYearAsc();

}
