package com.example.demo.services;

import com.example.demo.entities.Utente;
import com.example.demo.repositories.RepositoryUtente;
import com.example.demo.support.MailNotSpecifiedException;
import com.example.demo.support.MailUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceAccount {
    @Autowired
    private RepositoryUtente repositoryUtente;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Utente registraUtente(Utente user) throws MailUserAlreadyExistsException, MailNotSpecifiedException {
        if(user.getEmail() == null){
            throw new MailNotSpecifiedException();
        }
        else if (repositoryUtente.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }
        return repositoryUtente.save(user);
    }

    @Transactional(readOnly = true)
    public List<Utente> getAllUsers() {
        return repositoryUtente.findAll();
    }


}
