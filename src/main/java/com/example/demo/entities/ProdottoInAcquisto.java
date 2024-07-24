package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name="prodotto_in_acquisto", schema = "dbServer")
public class ProdottoInAcquisto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "acquisto") //implemento il vincolo di chiave esterna tra la proprietà acquisto e l'id dell'acquisto corrispondente contenuto nella tabella Acquisto del mio DB.
    @JsonIgnore
    @ToString.Exclude //Escludo questo parametro dalla generazione del toString da parte di Lombok
    private Acquisto acquisto;

    @Basic
    @Column(nullable = false)
    private int quantitaAcquistata;

    @ManyToOne
    @JoinColumn(name = "prodotto")
    private Prodotto prodotto;
}
