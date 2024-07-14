package com.example.demo.controllers;

import com.example.demo.entities.Utente;
import com.example.demo.services.ServiceAccount;
import com.example.demo.support.MailNotSpecifiedException;
import com.example.demo.support.MailUserAlreadyExistsException;
import com.example.demo.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/utente")
public class ControllerUtente {
    @Autowired
    private ServiceAccount serviceAccount;

    @PostMapping
    public ResponseEntity creaUtente(@RequestBody @Valid Utente utente) {
        try {
            serviceAccount.registraUtente(utente);
            return new ResponseEntity<>(new ResponseMessage("Creazione utente avvenuta correttamente!"), HttpStatus.OK); //Oggetto fornito da Spring per fornire una risposta al client. Riceve in input un oggetto instanza di una entità e una info. sulla corretta esecuzione dell'operazione invocata dal client.
        } catch (MailUserAlreadyExistsException e) { //nel caso in cui l'operazione non vada a buon fine inoltro un'altro tipo di messaggio all'interno della ResponseEntity.
            return new ResponseEntity<>(new ResponseMessage("Account con questa email già esistente!"), HttpStatus.BAD_REQUEST);
        }catch (MailNotSpecifiedException n){
            return new ResponseEntity<>(new ResponseMessage("Impossibile effettuare la registrazione causa email non specificata!"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Utente> listaUtenti() {
        return serviceAccount.getAllUsers();
    }


}
