package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicParser {

    public List<Sentence> parseFile( InputStream inputstream) throws IOException {
        List<Sentence> sentences = new ArrayList<Sentence>();

        Sentence currentSentence = new Sentence();
        StringBuilder currentToken = new StringBuilder();


        String punctuation = "()\"?,'";


        int data = inputstream.read();
        while(data != -1) {
            char c = (char) data;
            if (c == '\n'){
                // ignore line break

            } else if (c == '.'){
                if (currentToken.length() > 0 &&  currentToken.charAt( currentToken.length() - 1) == '.'){

                } else {
                    saveNonZeroLengthTokens(currentSentence, currentToken);
                }

                if (currentSentence.getTokens().size() > 0) {
                    sentences.add(currentSentence);
                    currentSentence = new Sentence();
                }

                currentToken.append(c);
            } else if (c == ' '){

                saveNonZeroLengthTokens(currentSentence, currentToken);

            } else if (punctuation.indexOf(c) >= 0){
                // this is punctuation
                saveNonZeroLengthTokens(currentSentence, currentToken);
                Token t = new Token( String.valueOf(c));
                currentSentence.getTokens().add(t);

            } else {
                // not punctionation, whitespace or period
                currentToken.append(c);
            }



            data = inputstream.read();
        }

        if (currentToken.length() > 0){
            Token t = new Token(currentToken.toString());
            currentSentence.getTokens().add(t);
        }
        if (currentSentence.getTokens().size() > 0){
            sentences.add(currentSentence);
        }



        inputstream.close();





        return sentences;
    }
    private void saveNonZeroLengthTokens(Sentence currentSentence, StringBuilder currentToken) {
        if (currentToken.length() > 0) {
            if (currentToken.charAt(currentToken.length() - 1) != '.') {
                Token t = new Token(currentToken.toString());
                currentSentence.getTokens().add(t);
            }
            currentToken.setLength(0);
        }
    }

}
