package com.digitalreasoning.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class Sentence {
    List<Token> tokens = new ArrayList<Token>();

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
