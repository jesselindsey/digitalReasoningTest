package com.digitalreasoning.entities;

/**
 * Created by lindsey on 9/20/16.
 */
public class Token {
    public Token(String value) {
        this.value = value;
    }

    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
