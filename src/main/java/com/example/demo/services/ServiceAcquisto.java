package com.example.demo.services;

import com.example.demo.entities.Acquisto;
import com.example.demo.entities.Prodotto;
import com.example.demo.entities.ProdottoInAcquisto;
import com.example.demo.entities.Utente;
import com.example.demo.repositories.RepositoryAcquisti;
import com.example.demo.repositories.RepositoryProdotti;
import com.example.demo.repositories.RepositoryProdottoInAcquisto;
import com.example.demo.repositories.RepositoryUtente;
import com.example.demo.support.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAcquisto {
    @Autowired
    private RepositoryAcquisti repoAcquisti;
    @Autowired
    private RepositoryProdottoInAcquisto repoProdottoInAcquisto;
    @Autowired
    private RepositoryUtente repoUtente;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RepositoryProdotti repoProdotti;


    //Metodo che implementa il servizio di acquisto di un prodotto:
    //(!L'oggetto Acquisto contiene sia la lista dei prodotti acquistati ma anche la quantità per ciascun prodotto acquistato)
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Acquisto aggiungiAcquisto(int id, List<ProdottoInAcquisto> listaProdotti) throws QuantityProductUnavailableException, UserNotFoundException{
        //creazione acqusito dai dati ricevuti dal client:
        Acquisto acquisto = new Acquisto();
        if(!repoUtente.existsById(id)){
            throw new UserNotFoundException();
        }
        Utente acquirente= repoUtente.findById(id);
        acquisto.setAcquirente(acquirente);
        acquisto.setProdottiInAcquisto(listaProdotti);
        Acquisto risultato = repoAcquisti.save(acquisto); //rendo l'oggetto acquisto PERSISTENTE e mi prendo l'oggetto GESTITO
        for ( ProdottoInAcquisto p : risultato.getProdottiInAcquisto() ) {
            p.setAcquisto(risultato);
            ProdottoInAcquisto inserito = repoProdottoInAcquisto.save(p); //rendo l'oggetto ProdottoInAcquisto p PERSISTENTE all'interno della tabella prodotto_in_acquisto
            entityManager.refresh(inserito); //eseguo la refresh() affinche gli venga assegnato l'id univoco all'entità gestita
            Prodotto prodotto = repoProdotti.findById(inserito.getProdotto().getId());
            //DEBUG:
            System.out.println(prodotto.toString());
            int qtaRimanente =  prodotto.getQuantita() - p.getQuantitaAcquistata();
            if ( qtaRimanente < 0 ) {
                throw new QuantityProductUnavailableException(prodotto.getNome());
            }
            prodotto.setQuantita(qtaRimanente);
            entityManager.refresh(p);
        }
        entityManager.refresh(risultato);
        return risultato;
    }

    /*
    //Effettuo la rimozione del seguente acqusito andando a restituire in output al controller la lista degli acquisti
    //rimanenti che l'utente ha effettuato fino a quel momento: (metodo inutile poichè quando un utente ha effettuato un acquisto non può più annullarlo)
    public List<Acquisto> rimuoviAcquisto(int idAcquisto) throws AcquistoNotFoundException {
        if(!repoAcquisti.existsById(idAcquisto)) {
            throw new AcquistoNotFoundException();
        }
        Acquisto daRimuovere = repoAcquisti.getById(idAcquisto);
        Utente acquirente = daRimuovere.getAcquirente();
        repoAcquisti.deleteById(idAcquisto);
        return acquirente.getListaAcquisti();
    }
     */

    @Transactional(readOnly = true)
    public List<Acquisto> getAllAcquistiUtente(Utente user) throws UserNotFoundException, EmptyList {
        if ( !repoUtente.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        List<Acquisto> risultato= repoAcquisti.findByAcquirente(user);
        if( risultato.isEmpty() ) {
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Acquisto> getAcquistiUtentePerPeriodo(Utente user, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException, EmptyList {
        if ( !repoUtente.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) > 0 ) {
            throw new DateWrongRangeException();
        }
        List<Acquisto> risultato= repoAcquisti.findByAcquirenteInPeriodo(startDate, endDate, user);
        if( risultato.isEmpty() ) {
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Acquisto> getAcquistiUtentePerData(Utente user, Date date) throws UserNotFoundException, EmptyList {
        if ( !repoUtente.existsById(user.getId()) ) {
            throw new UserNotFoundException();
        }
        List<Acquisto> risultato= repoAcquisti.findByAcquirenteInData(date, user);
        if( risultato.isEmpty() ) {
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Acquisto> getAllAcquisti(){
        return repoAcquisti.findAll();
    }

}

/*
    //Metodo che implementa il servizio di acquisto di un prodotto:
    //(!L'oggetto Acquisto contiene sia la lista dei prodotti acquistati ma anche la quantità per ciascun prodotto acquistato)
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Acquisto aggiungiAcquisto(Acquisto acquisto) throws QuantityProductUnavailableException{
        Acquisto risultato = repoAcquisti.save(acquisto); //rendo l'oggetto acquisto PERSISTENTE e mi prendo l'oggetto GESTITO
        for ( ProdottoInAcquisto p : risultato.getProdottiInAcquisto() ) {
            p.setAcquisto(risultato);
            ProdottoInAcquisto inserito = repoProdottoInAcquisto.save(p); //rendo l'oggetto ProdottoInAcquisto p PERSISTENTE all'interno della tabella prodotto_in_acquisto
            entityManager.refresh(inserito); //eseguo la refresh() affinche gli venga assegnato l'id univoco all'entità gestita
            Prodotto prodotto = repoProdotti.findById(inserito.getProdotto().getId());
            //DEBUG:
            System.out.println(prodotto.toString());
            int qtaRimanente =  prodotto.getQuantita() - p.getQuantitaAcquistata();
            if ( qtaRimanente < 0 ) {
                throw new QuantityProductUnavailableException(prodotto.getNome());
            }
            prodotto.setQuantita(qtaRimanente);
            entityManager.refresh(p);
        }
        entityManager.refresh(risultato);
        return risultato;
    }
 */
