package com.example.demo.controllers;

import com.example.demo.entities.Acquisto;
import com.example.demo.entities.Utente;
import com.example.demo.services.ServiceAcquisto;
import com.example.demo.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/acquisti")
public class ControllerAcquisto {
    @Autowired
    private ServiceAcquisto serviceAcquisto;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity aggiungiAcquisto(@RequestBody @Valid Acquisto acquisto) { // è buona prassi ritornare l'oggetto inserito
        try {
            Acquisto risultato= serviceAcquisto.aggiungiAcquisto(acquisto);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        } catch (QuantityProductUnavailableException e) {
            return new ResponseEntity<>(new ResponseMessage("La quantità del prodotto " + e.getNomeProdotto() + " non è disponibile!"), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    //Effettua la rimozione dai dati persistenti dell'acquisto con id passato in input dal client andando a restituire
    //la lista degli acquisti effettuati dall'utente che era "acquirente" dell'acquisto rimosso:
    @DeleteMapping("/rimuovi")
    public ResponseEntity rimuoviAcquisto(@RequestParam(value = "id") int id) {
        try{
            List<Acquisto> listaAcquisti = serviceAcquisto.rimuoviAcquisto(id);
            return new ResponseEntity<>(listaAcquisti, HttpStatus.OK);
        }catch (AcquistoNotFoundException e){
            return new ResponseEntity<>(new ResponseMessage("Acquisto non trovato!"), HttpStatus.NOT_FOUND);
        }
    }
    */

    @GetMapping("/{user}")
    public List<Acquisto> getAcquistiUtente(@PathVariable Utente user) {
        try {
            return serviceAcquisto.getAllAcquistiUtente(user);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato!", e);
        } catch (EmptyList em){
            throw new ResponseStatusException(HttpStatus.OK, "Questo Utente non ha effettuato alcun acquisto!", em);
        }
    }

    @GetMapping("/{user}/{startDate}/{endDate}") //per invocare questi metodo bisognerà indicare id dell'utente, la data di inzio e la data di fine.
    public ResponseEntity getAcquistiUtenteInPeriod(@PathVariable("user") Utente user, @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date start, @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date end) {
        try {
            List<Acquisto> risultato = serviceAcquisto.getAcquistiUtentePerPeriodo(user, start, end);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "l'Utente indicato non esiste!", e);
        } catch (DateWrongRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La data di inizio deve essere inferiore alla data di fine!", e);
        }catch (EmptyList e){
            return new ResponseEntity<>(new ResponseMessage("L'Utente non ha effettuato acquisti nel periodo indicato!"), HttpStatus.OK);
        }
    }

    @GetMapping("/{user}/{Date}")
    public ResponseEntity getAcquistiUtentePerData(@PathVariable("user") Utente user, @PathVariable("Date") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
        try{
            List<Acquisto> risultato= serviceAcquisto.getAcquistiUtentePerData(user, date);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "l'Utente indicato non esiste!", e);
        }catch (EmptyList e){
            return new ResponseEntity<>(new ResponseMessage("L'Utente non ha effettuato acquisti in data: "+date.toString()), HttpStatus.OK);
        }
    }

    @GetMapping
    public List<Acquisto> getAllAcquisti(){
            return serviceAcquisto.getAllAcquisti();
    }

}
