package com.digitalreasoning.entities;

/**
 * Created by lindsey on 9/20/16.
 */
public class Token {
    public boolean isNamedEntity;
    public Token(String value) {
        this.value = value;
    }

    String value;

    public boolean isNamedEntity() {
        return isNamedEntity;
    }

    public void setNamedEntity(boolean namedEntity) {
        isNamedEntity = namedEntity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
