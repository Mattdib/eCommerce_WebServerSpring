package com.example.demo.repositories;

import com.example.demo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryUtente extends JpaRepository<Utente, Integer> {

    //ricerca per proprietà Utente:
    List<Utente> findByNome(String nome);
    List<Utente> findByCognome(String cognome);
    List<Utente> findByNomeAndCognome(String nome, String cognome);
    List<Utente> findByEmail(String email);
    List<Utente> findByCodice(String codice);
    boolean existsByEmail(String email);

}
