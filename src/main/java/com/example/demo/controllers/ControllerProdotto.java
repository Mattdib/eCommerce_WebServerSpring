package com.example.demo.controllers;

import com.example.demo.entities.Prodotto;
import com.example.demo.services.ServiceProdotto;
import com.example.demo.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ControllerProdotto {
    @Autowired
    private ServiceProdotto serviceProdotto;

    //servizi invocabili solo da utenti che sono anche venditori:

    @PostMapping
    public ResponseEntity aggiungi(@RequestBody @Valid Prodotto prodotto) {
        try {
            serviceProdotto.aggiungiProdotto(prodotto);
        } catch (BarCodeAlreadyExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Prodotto con lo stesso barCode già esistente!"), HttpStatus.BAD_REQUEST);
        }
        catch (BarCodeIsNotDefined b){
            return new ResponseEntity<>(new ResponseMessage("Specificare il barCode del prodotto da aggiungere!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("aggiunto prodotto al DB!"), HttpStatus.OK);
    }

    @DeleteMapping("/rimuovi")
    public ResponseEntity rimuovi(@RequestParam(value = "barCode") String barCode) {
        try {
            serviceProdotto.rimuoviProdotto(barCode);
        } catch (BarCodeNotExistException e) {
            return new ResponseEntity<>(new ResponseMessage("Non esiste alcun prodotto con questo barCode!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("rimosso prodotto con barCode: "+barCode+" dal DB!"), HttpStatus.OK);
    }

    //servizi di ricerca invocabili da tutti gli utenti:

    @GetMapping("/listaProd")
    public List<Prodotto> visualizzaTutti() {
        return serviceProdotto.mostraListaProdotti();
    }

    @GetMapping("/listaProdPaginata")
    public ResponseEntity visualizzaTuttiPaginata(@RequestParam(value = "numeroPagina", defaultValue = "0") int numeroPagina, @RequestParam(value = "dimPagina", defaultValue = "10") int dimPagina, @RequestParam(value = "ordinaPer", defaultValue = "id") String ordinaPer) {
        List<Prodotto> risultato = serviceProdotto.mostraListaProdotti(numeroPagina, dimPagina, ordinaPer);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/ricercaPerNome")
    public ResponseEntity visualizzaPerNome(@RequestParam(value = "name") String nome) {
        List<Prodotto> risultato = serviceProdotto.mostraProdottiPerNome(nome);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }

    @GetMapping("/ricercaPerBarCode/{barCode}")
    public ResponseEntity visualizzaPerBarCode(@PathVariable String barCode) {
        try {
            Prodotto risultato = serviceProdotto.mostraPerBarCode(barCode);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (BarCodeNotExistException e){
            return new ResponseEntity<>(new ResponseMessage("Non esiste un prodotto con questo BarCode!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prodottiPerPrezzoMax")
    public ResponseEntity visualizzaPerPrezzoMax(@RequestParam(value = "prezzomax") float prezzomax) {
        try {
            List<Prodotto> risultato = serviceProdotto.mostraProdottiPerPrezzoMassimo(prezzomax);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (EmptyList e){
            return new ResponseEntity<>(new ResponseMessage("Non esiste un prodotto al di sotto del prezzo indicato!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prodottiPerPrezzoMin")
    public ResponseEntity visualizzaPerPrezzoMin(@RequestParam(value = "prezzomin") float prezzomin) {
        try {
            List<Prodotto> risultato = serviceProdotto.mostraProdottiPerPrezzoMinimo(prezzomin);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (EmptyList e){
            return new ResponseEntity<>(new ResponseMessage("Non esiste un prodotto al di sopra del prezzo indicato!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/prodottiPerIntervalloPrezzo")
    public ResponseEntity visualizzaPerIntervallo(@RequestParam(value = "prezzomin") float prezzomin, @RequestParam(value = "prezzomax") float prezzomax) {
        try {
            List<Prodotto> risultato = serviceProdotto.mostraProdottiPerIntervalloPrezzo(prezzomin, prezzomax);
            return new ResponseEntity<>(risultato, HttpStatus.OK);
        }catch (EmptyList e){
            return new ResponseEntity<>(new ResponseMessage("Non esiste un prodotto in questa fascia di prezzo!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ricercaAvanzataProdotti")
    public ResponseEntity ricercaAvanzata(@RequestParam(value = "nome", required = false) String nome,
                                          @RequestParam(value = "descrizione", required = false) String descrizione,
                                          @RequestParam(value = "prezzomin", required = false) Float prezzomin,
                                          @RequestParam(value = "prezzomax", required = false) Float prezzomax,
                                          @RequestParam(value = "type", required = false) String type){
        List<Prodotto> risultato = serviceProdotto.ricercaAvanzata(nome, descrizione, prezzomin, prezzomax, type);
        return new ResponseEntity<>(risultato, HttpStatus.OK);
    }


}//ControllerProdotto
