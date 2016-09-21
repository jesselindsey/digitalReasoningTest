package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.serializer.BasicXMLSerializer;
import com.digitalreasoning.serializer.XmlSerializer;
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
    XmlSerializer serializer = new BasicXMLSerializer();


    @Test
    public void parseFile_whenGivenTestFile_thenAllSymbolsAreTokenized() throws IOException {
        validateSentenceParsing("word ."   , parser, /*Sentences=*/ 1, /*Total Tokens*/1);
        validateSentenceParsing("word ..." , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word."    , parser, /*Sentences=*/ 1, /*Total Tokens*/1);
        validateSentenceParsing("word..."  , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word,."   , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word,..." , parser, /*Sentences=*/ 1, /*Total Tokens*/3);

        validateSentenceParsing("word\n ."   , parser, /*Sentences=*/ 1, /*Total Tokens*/1);
        validateSentenceParsing("word\n ..." , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word\n."    , parser, /*Sentences=*/ 1, /*Total Tokens*/1);
        validateSentenceParsing("word\n..."  , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word\n,."   , parser, /*Sentences=*/ 1, /*Total Tokens*/2);
        validateSentenceParsing("word\n,..." , parser, /*Sentences=*/ 1, /*Total Tokens*/3);


    }

    public void validateSentenceParsing(String str, BasicParser basicParser, int expectedSentences, int expectedTokens) throws IOException {
        System.out.println(String.format ("Testing: '%s'",str));
        List<Sentence> sentences = basicParser.parseFile(getInputStream(str));
        System.out.println( serializer.serializeSentences(sentences) );

        int totalTokens      = 0;
        for ( Sentence s: sentences){
            totalTokens += s.getTokens().size();
        }
        assertEquals( "Expected Sentences",expectedSentences ,sentences.size()  );
        assertEquals( "Expected Tokens"   ,expectedTokens   ,totalTokens       );
    }

    @Test
    public void parseFile_whenGivenTestFile_thenWordsExistInSentence(){

    }


    @Test
    public void parseFile_givenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("..."));
        assertEquals( sentences.size(),1);
        assertEquals( sentences.get(0).getTokens().size(),1);
    }

    @Test
    public void parseFile_SentenceEndingWithPeriod_ReturnsTwoSentences() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("I am going to the store.  It is hot"));

        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );

        assertEquals( sentences.size(),2);
        assertEquals( sentences.get(0).getTokens().size(),6);
        assertEquals( sentences.get(1).getTokens().size(),3);


    }
    @Test
    public void parseFile_GivenSentence_EveryWordIsTokenized() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("I am going to the store.  It is hot"));
        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );

        assertEquals( sentences.get(0).getTokens().size(),6);



    }

    @Test
    public void parseFile_whenGivenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream("..."));
        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }
    @Test
    public void parseFile_whenGivenSpaceThenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseFile(getInputStream(" ..."));
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }

    InputStream getInputStream( String s){
        return new ByteArrayInputStream(s.getBytes());
    }
}