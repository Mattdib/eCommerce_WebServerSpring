package com.example.demo.repositories;

import com.example.demo.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryProdotti extends JpaRepository<Prodotto, Integer> {

    //Ricerca per proprietà del Prodotto:
    List<Prodotto> findByNomeContaining(String name);

    Prodotto findByBarCode(String barCode);

    boolean existsByBarCode(String barCode);

    Prodotto findById(int id);

    //Ricerche pre prezzo:
    @Query("select p from Prodotto p where p.prezzo <= ?1")
    List<Prodotto> findByPrezzoMassimo(float prezzo);

    @Query("select p from Prodotto p where p.prezzo >= ?1")
    List<Prodotto> findByPrezzoMinimo(float prezzo);

    @Query("select p from Prodotto p where p.prezzo >= ?1 and p.prezzo <= ?2")
    List<Prodotto> findByIntervalloPrezzo(float prezzomin, float prezzomax);

    //Ricerca avanzata:
    @Query("SELECT p " +
            "FROM Prodotto p " +
            "WHERE (p.nome LIKE ?1 OR ?1 IS NULL) AND " +
            "      (p.descrizione LIKE ?2 OR ?2 IS NULL) AND" +
            "      (p.prezzo >= ?3 OR ?3 IS NULL) AND (p.prezzo <= ?4 OR ?4 IS NULL)")
    List<Prodotto> advancedSearch(String name, String description, Float prezzomin, Float prezzomax);
}
