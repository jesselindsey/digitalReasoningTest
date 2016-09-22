package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicParser {



    String punctuation = "()\"?,':";

    enum TokenType{
        EMPTY,WHITESPACE,DOT,PUNCTUATION,CHARACTER, IGNORE, NUMBER;

        public boolean isTokenizeAble() {
            return this==DOT || this==PUNCTUATION || this==CHARACTER || this == NUMBER;
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
        boolean reachedEOS = false;
         while (!reachedEOS) {
             if (data < 0){
                 reachedEOS = true;
             }
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
            } else if (Character.isDigit(c)) {
                currentTokenType = TokenType.NUMBER;
            } else {
                currentTokenType = TokenType.CHARACTER;
            }


            if (reachedEOS || lastTokenType != TokenType.WHITESPACE && currentTokenType == TokenType.WHITESPACE && currentToken.length() > 0){
                // we are at a whitespace break

                String tokenStr = currentToken.toString();
                currentToken.setLength(0);
                if (tokenStr.matches("^\\w+$")) {
                    /*word*/
                    currentSentence.getTokens().add(new Token(tokenStr));
                } else if (tokenStr.matches("^\\w+'\\w+$")){
                    /*word's*/
                        currentSentence.getTokens().add( new Token(tokenStr));
                } else if (tokenStr.matches("^\\d+\\.\\d+$")){
                    /*2.12*/
                    currentSentence.getTokens().add( new Token(tokenStr));
                } else if (tokenStr.matches("(\\w+)(\\.)(\\w+)")){

                    /*word.word*/
                    currentSentence.getTokens().add( new Token(tokenStr) );


                } else if (tokenStr.matches("^\\w+\\.$")){
                    currentSentence.getTokens().add( new Token(tokenStr.substring(0,tokenStr.length() - 1)));
                    currentSentence.getTokens().add( new Token("."));
                    if (currentSentence.getTokens().size() > 0){
                        sentences.add(currentSentence);
                        currentSentence = new Sentence();
                    }

                } else if (tokenStr.matches("^(\\w+)(\\.\\.+)$")){
                    Pattern p = Pattern.compile("^(\\w+)(\\.\\.+)$");
                    Matcher m = p.matcher(tokenStr);
                    m.find();

                    currentSentence.getTokens().add( new Token(m.group(1)));
                    currentSentence.getTokens().add( new Token(m.group(2)));
                    if (currentSentence.getTokens().size() > 0){
                        sentences.add(currentSentence);
                        currentSentence = new Sentence();
                    }

                } else if (tokenStr.matches("^(\\w+)([,])(\\.)$")){
                    Pattern p = Pattern.compile("^(\\w+)([,]+)(\\.)$");
                    Matcher m = p.matcher(tokenStr);
                    m.find();

                    currentSentence.getTokens().add( new Token(m.group(1)));
                    currentSentence.getTokens().add( new Token(m.group(2)));
                    currentSentence.getTokens().add( new Token(m.group(3)));
                    if (currentSentence.getTokens().size() > 0){
                        sentences.add(currentSentence);
                        currentSentence = new Sentence();
                    }

                } else if (tokenStr.matches("^(\\w+)([,])(\\.\\.+)$")){
                    /*"word,..."*/
                    Pattern p = Pattern.compile("^(\\w+)([,]+)(\\.\\.+)$");
                    Matcher m = p.matcher(tokenStr);
                    m.find();

                    currentSentence.getTokens().add( new Token(m.group(1)));
                    currentSentence.getTokens().add( new Token(m.group(2)));
                    currentSentence.getTokens().add( new Token(m.group(3)));
                    if (currentSentence.getTokens().size() > 0){
                        sentences.add(currentSentence);
                        currentSentence = new Sentence();
                    }
                }else if (tokenStr.matches("[\\.]+")){

                    currentSentence.getTokens().add( new Token(tokenStr));
                }else if (tokenStr.matches("[^\\.]+")){

                    StringBuilder currentWord = new StringBuilder();
                    for (int i = 0; i < tokenStr.length(); i++) {
                        String currentLetter = tokenStr.substring(i,i+1);
                        if ( currentLetter.matches("\\p{Punct}" )){
                            if (currentWord.length() > 0){
                                currentSentence.getTokens().add( new Token( currentWord.toString()  ));
                                currentWord.setLength(0);
                            }
                            currentSentence.getTokens().add( new Token( currentLetter  ));

                        } else {
                            currentWord.append(currentLetter);
                        }


                    }
                    if (currentWord.length() > 0){
                        currentSentence.getTokens().add( new Token( currentWord.toString()  ));
                        currentWord.setLength(0);
                    }

                }

                if ( reachedEOS || tokenStr.matches("\\.") ){
                    if (currentSentence.getTokens().size() > 0){
                        sentences.add(currentSentence);
                        currentSentence = new Sentence();
                    }

                }
            }
            if (currentTokenType != TokenType.WHITESPACE ) {
                currentToken.append(c);
            }
            lastTokenType = currentTokenType;

            data = inputStreamReader.read();
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
