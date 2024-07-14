package com.example.demo.support;

public class QuantityProductUnavailableException extends Exception{
    private final String nomeProdotto;

    public QuantityProductUnavailableException(String nomeProdotto){
        this.nomeProdotto = nomeProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }
}
