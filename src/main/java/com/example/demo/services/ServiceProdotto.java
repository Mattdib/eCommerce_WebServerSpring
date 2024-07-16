package com.example.demo.services;

import com.example.demo.entities.Prodotto;
import com.example.demo.repositories.RepositoryProdotti;
import com.example.demo.support.BarCodeAlreadyExistException;
import com.example.demo.support.BarCodeIsNotDefined;
import com.example.demo.support.BarCodeNotExistException;
import com.example.demo.support.EmptyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProdotto {
    @Autowired
    private RepositoryProdotti repositoryProdotti;

    //Operazioni di modifica ai dati del DB: (Accessibili sono dai venditori)

    @Transactional(readOnly = false)
    public void aggiungiProdotto(Prodotto prodotto) throws BarCodeAlreadyExistException, BarCodeIsNotDefined {
        if (prodotto.getBarCode() == null){
            throw new BarCodeIsNotDefined();
        }
        else if (repositoryProdotti.existsByBarCode(prodotto.getBarCode()) ) {
            throw new BarCodeAlreadyExistException(); //inoltro l'eccezione al controller che mi ha invocato
        }
        else repositoryProdotti.save(prodotto);
    }

    @Transactional(readOnly = false)
    public void rimuoviProdotto(String barCode) throws BarCodeNotExistException{
        if (!repositoryProdotti.existsByBarCode(barCode)) {
            throw new BarCodeNotExistException(); //inoltro l'eccezione al controller che mi ha invocato
        }
        Prodotto prodotto = repositoryProdotti.findByBarCode(barCode); //rimuovo il prodotto dal DB
        repositoryProdotti.delete(prodotto);
    }

    //Operazioni di accesso ai dati del DB: (Accessibili a tutti i tipi di utenti)

    @Transactional(readOnly = true)
    public List<Prodotto> mostraListaProdotti() {
        return repositoryProdotti.findAll();
    }

    //metodo da per implementare le pagine da far visualizzare all'utente sul client: (definisco quali e quanti prodotti mostrare per ogni pagina)
    @Transactional(readOnly = true)
    public List<Prodotto> mostraListaProdotti(int numPagina, int dimPagina, String ordinaPer) {
        Pageable paging = PageRequest.of(numPagina, dimPagina, Sort.by(ordinaPer));
        Page<Prodotto> pagedResult = repositoryProdotti.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else{
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPerNome(String name) {
        List<Prodotto> risultato = repositoryProdotti.findByNomeContaining(name);
        return risultato;
    }

    @Transactional(readOnly = true)
    public Prodotto mostraPerBarCode(String barCode) throws BarCodeNotExistException {
        if (!repositoryProdotti.existsByBarCode(barCode)) {
            throw new BarCodeNotExistException(); //inoltro l'eccezione al controller che mi ha invocato
        }
        return repositoryProdotti.findByBarCode(barCode);
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPerPrezzoMassimo(float prezzo) throws EmptyList {
        List<Prodotto> risultato = repositoryProdotti.findByPrezzoMassimo(prezzo);
        if(risultato.isEmpty()){
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPerPrezzoMinimo(float prezzo) throws EmptyList {
        List<Prodotto> risultato = repositoryProdotti.findByPrezzoMinimo(prezzo);
        if(risultato.isEmpty()){
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> mostraProdottiPerIntervalloPrezzo(float prezzomin, float prezzomax) throws EmptyList {
        List<Prodotto> risultato = repositoryProdotti.findByIntervalloPrezzo(prezzomin, prezzomax);
        if(risultato.isEmpty()){
            throw new EmptyList();
        }
        return risultato;
    }

    @Transactional(readOnly = true)
    public List<Prodotto> ricercaAvanzata(String nome,String descrizione, Float prezzomin, Float prezzomax) throws EmptyList {
        List<Prodotto> risultato = repositoryProdotti.advancedSearch(nome, descrizione, prezzomin, prezzomax);
        if(risultato.isEmpty()){
            throw new EmptyList();
        }
        return risultato;
    }

}

