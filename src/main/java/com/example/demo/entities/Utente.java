package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "utente", schema = "testdatabase")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = true, length = 70) //!il campo (lenght) deve COINCIDERE con quello SPECIFICATO NEL FILE SQL dove definisco le tabelle del mio DB
    private String codice;

    @Column(nullable = true, length = 50)
    private String nome;

    @Column(nullable = true, length = 50)
    private String cognome;

    @Column(nullable = true, length = 20)
    private String telefono;

    @Column(nullable = false, length = 90)
    private String email;

    @Column(nullable = true, length = 150)
    private String indirizzo;

    @OneToMany(mappedBy = "acquirente", cascade = CascadeType.MERGE)
    @JsonIgnore//!specifico che questa proprietà deve essere IGNORATA quando io vado a generare la STRINGA JSON da trasmettere al client quando gli invio delle informazioni tramite una risposta
    private List<Acquisto> listaAcquisti; //lista contenente tutti gli acquisti effettuati da questo acquirente/utente

}
