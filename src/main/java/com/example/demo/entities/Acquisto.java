package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "acquisto", schema = "dbServer")
public class Acquisto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAcquisto;

    @ManyToOne
    @JoinColumn(name = "acquirente")
    private Utente acquirente; //utente che ha effettuato l'acquisto

    @OneToMany(targetEntity = ProdottoInAcquisto.class, mappedBy = "acquisto", cascade = CascadeType.ALL)
    private List<ProdottoInAcquisto> prodottiInAcquisto;


}
