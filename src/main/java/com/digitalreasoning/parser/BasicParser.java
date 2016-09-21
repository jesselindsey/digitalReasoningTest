package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicParser {



    String punctuation = "()\"?,'";

    enum TokenType{
        EMPTY,WHITESPACE,DOT,PUNCTUATION,CHARACTER, IGNORE;

        public boolean isTokenizeAble() {
            return this==DOT || this==PUNCTUATION || this==CHARACTER;
        }
    }

    public List<Sentence> parseString( String str) throws IOException {
        InputStream is = getInputStream(str);
        List<Sentence> result = parseStream(is);
        is.close();
        return result;
    }

    private InputStream getInputStream( String s){
        return new ByteArrayInputStream(s.getBytes());
    }
    public List<Sentence> parseStream(InputStream is) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(is, "utf8");

        List<Sentence> sentences = new ArrayList<Sentence>();

        Sentence currentSentence = new Sentence();
        StringBuilder currentToken = new StringBuilder();


        TokenType lastTokenType     = TokenType.EMPTY;
        TokenType currentTokenType  = TokenType.EMPTY;
        int data = inputStreamReader.read();
         while (data > -1) {
            char c = (char) data;

            // determine the token type
            if (c == '\n'){
                currentTokenType = TokenType.IGNORE;
            } else if (c == '.'){
                currentTokenType = TokenType.DOT;
            } else if (c == ' '){
                currentTokenType = TokenType.WHITESPACE;
            } else if (punctuation.indexOf(c) >= 0){
                currentTokenType = TokenType.PUNCTUATION;
            } else {
                currentTokenType = TokenType.CHARACTER;
            }

            if (lastTokenType == currentTokenType   ){
                // Case the the token type is the same
                if (currentTokenType.isTokenizeAble()) {
                    if (currentTokenType == TokenType.PUNCTUATION) {
                        // each punctuation is duplicated
                        saveNonZeroLengthTokens(currentSentence, currentToken);
                    }
                    currentToken.append(c);
                }
            } else {
                // Case the the token type changes
                if  (lastTokenType.isTokenizeAble() ) {
                    if (lastTokenType != TokenType.DOT  ||
                        lastTokenType == TokenType.DOT && currentToken.length() > 1){
                        saveNonZeroLengthTokens(currentSentence, currentToken);
                    }else {
                        // start a new sentence
                        if (currentSentence.getTokens().size() > 0){
                            sentences.add(currentSentence);
                            currentSentence = new Sentence();
                        }
                    }

                }
                currentToken.setLength(0);
                currentToken.append(c);
                lastTokenType = currentTokenType;
            }


            data = inputStreamReader.read();
        }

        if  (lastTokenType.isTokenizeAble() ) {
            if (lastTokenType != TokenType.DOT  ||
                    lastTokenType == TokenType.DOT && currentToken.length() > 1){
                saveNonZeroLengthTokens(currentSentence, currentToken);
            }
        }
        if (currentSentence.getTokens().size() > 0){
            sentences.add(currentSentence);
        }


        inputStreamReader.close();


        return sentences;
    }
    private void saveNonZeroLengthTokens(Sentence currentSentence, StringBuilder currentToken) {
        if (currentToken.length() > 0) {

            Token t = new Token(currentToken.toString());

            currentSentence.getTokens().add(t);

        }
    }


}
