package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.serializer.XMLSerializer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicParserTest {
    BasicParser parser = new BasicParser();



    //It should correctly process all symbols, including punctuation and whitespace.
    @Test
    public void parseFile_whenGivenTestFile_thenAllSymbolsAreTokenized(){

    }

    //Every word must fall into a sentence
    @Test
    public void parseFile_whenGivenTestFile_thenWordsExistInSentence(){

    }

    //    When    your    program    runs    it    should
    //    output  an  XML  representation  of  your  Java  object  model.

    //It should correctly process all symbols, including punctuation and whitespace.
    @Test
    public void parseFile_givenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("..."));
        assertEquals( sentences.size(),1);
        assertEquals( sentences.get(0).getTokens().size(),1);
    }

    @Test
    public void parseFile_SentenceEndingWithPeriod_ReturnsTwoSentences() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("I am going to the store.  It is hot"));

        System.out.println( new XMLSerializer().serializeSentence(sentences) );

        assertEquals( sentences.size(),2);
        assertEquals( sentences.get(0).getTokens().size(),6);
        assertEquals( sentences.get(1).getTokens().size(),3);


    }
    @Test
    public void parseFile_GivenSentence_EveryWordIsTokenized() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("I am going to the store.  It is hot"));
        System.out.println( new XMLSerializer().serializeSentence(sentences) );

        assertEquals( sentences.get(0).getTokens().size(),6);



    }

    @Test
    public void parseFile_whenGivenThreeOrMoreRepeatingDotsTokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("..."));
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }

    InputStream getInputStream( String s){
        return new ByteArrayInputStream(s.getBytes());
    }
}