package com.example.demo.repositories;

import com.example.demo.entities.Acquisto;
import com.example.demo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RepositoryAcquisti extends JpaRepository<Acquisto, Integer> {
    //Ricerca per proprietà Acquisto:
    List<Acquisto> findByAcquirente(Utente utente);
    List<Acquisto> findByDataAcquisto(Date data);
    void deleteById(int id);

    //Ricerca per periodo di acquisto:
    @Query("select p from Acquisto p where p.dataAcquisto > ?1 and p.dataAcquisto < ?2 and p.acquirente = ?3")
    List<Acquisto> findByAcquirenteInPeriodo(Date startDate, Date endDate, Utente user);

    //Ricerca per specifica data:
    @Query("select p from Acquisto p where p.dataAcquisto = ?1 and p.acquirente = ?2")
    List<Acquisto> findByAcquirenteInData(Date date, Utente user);

}
