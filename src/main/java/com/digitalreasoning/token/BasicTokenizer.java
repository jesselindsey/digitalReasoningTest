package com.digitalreasoning.token;

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
public class BasicTokenizer {
    private boolean lenient = true;

    public BasicTokenizer() {
    }

    public BasicTokenizer(boolean lenient) {
        this.lenient = lenient;
    }

    String punctuation = "()\"?,':";

    enum TokenType{
        EMPTY,WHITESPACE,DOT,PUNCTUATION,CHARACTER, IGNORE, NUMBER;
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

    Pattern multipleEndingDotPattern = Pattern.compile("([^\\.]+)(\\.\\.+)");
    Pattern endSentenceQuotes = Pattern.compile("([^\\.]+)\\.\\\"");
    Pattern decimalPattern = Pattern.compile("(\\p{Punct})*(\\d+\\.\\d+)");

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
//                currentTokenType = TokenType.IGNORE;
                currentTokenType = TokenType.WHITESPACE;
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
                 if (tokenStr.matches("^\\w+'\\w+$")){
                    /*word's*/
                        currentSentence.getTokens().add( new Token(tokenStr));
                 }else if (tokenStr.contains("-")){
                     //"Austria-Hungary"
                     currentSentence.getTokens().add( new Token(tokenStr));
                     sentences.add(currentSentence);
                     currentSentence = new Sentence();

                 } else if (decimalPattern.matcher( tokenStr ).find() ){
                     // "(2.12" , "2.12" ,"$2.12"
                     Matcher m = decimalPattern.matcher(tokenStr);
                     m.find();

                    if ( m.group(1) != null) {
                        currentSentence.getTokens().add(new Token(m.group(1)));
                    }
                     currentSentence.getTokens().add( new Token( m.group(2)));

                 } else if (tokenStr.matches("^\\$\\d+\\.\\d+$")){
                    /*2.12*/
                     currentSentence.getTokens().add( new Token(tokenStr));

                 } else if (tokenStr.matches("(\\w+)(\\.)(\\w+)")){
                    /*word.word*/
                    currentSentence.getTokens().add( new Token(tokenStr) );

                }else if (tokenStr.matches("[\\.]+")){
//                    "."
                    currentSentence.getTokens().add( new Token(tokenStr));
                }else if (tokenStr.matches("[^\\.]+")){
                    handleTokenWithoutPeriod(currentSentence, tokenStr);

                 }else if (endSentenceQuotes.matcher(tokenStr).find()){
                     /* word." */
                     Matcher m = endSentenceQuotes.matcher(tokenStr);
                     m.find();

                     handleTokenWithoutPeriod(currentSentence, m.group(1));
                     currentSentence.getTokens().add( new Token( ".") );
                     currentSentence.getTokens().add( new Token( "\"" ) );
                     sentences.add(currentSentence);
                     currentSentence = new Sentence();

                 }else if (multipleEndingDotPattern.matcher(tokenStr).find()){
                     /*word...*/
                     Matcher m = multipleEndingDotPattern.matcher(tokenStr);
                     m.find();

                     handleTokenWithoutPeriod(currentSentence, m.group(1));
                     currentSentence.getTokens().add( new Token( m.group(2)));
                     sentences.add(currentSentence);
                     currentSentence = new Sentence();
                }else if (tokenStr.matches("[^\\.]+\\.")){
                    handleTokenWithoutPeriod(currentSentence, tokenStr.substring(0,tokenStr.length() -1));
                    currentSentence.getTokens().add( new Token("."));
                    sentences.add(currentSentence);
                    currentSentence = new Sentence();

                } else if (tokenStr.equals( "" )){
                    // do nothing
                } else {
                    if (lenient){
                        if (tokenStr.length() > 0){
                            currentSentence.getTokens().add( new Token( tokenStr  ));
                        }
                    } else {
                        throw new RuntimeException("Token case not handled");
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

    private void handleTokenWithoutPeriod(Sentence currentSentence, String tokenStr) {
        // words with punctuation
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


    private void saveNonZeroLengthTokens(Sentence currentSentence, StringBuilder currentToken) {
        if (currentToken.length() > 0) {

            Token t = new Token(currentToken.toString());

            currentSentence.getTokens().add(t);

        }
    }


}
