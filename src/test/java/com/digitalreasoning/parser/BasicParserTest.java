package com.digitalreasoning.parser;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.serializer.BasicXMLSerializer;
import com.digitalreasoning.serializer.XmlSerializer;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicParserTest {
    BasicParser parser = new BasicParser();
    XmlSerializer serializer = new BasicXMLSerializer();



    int maxPhraseLength,maxActualLength,maxExpectedLength;
    class Result{
        String phrase, expected, result;


    }
    List<Result> results = new ArrayList<Result>();
    public void validate(String phrase, String expected){
        Result r = new Result();
        r.phrase= phrase;
        r.expected = expected;

        try {
            r.result =  serializer.serializeSentences( parser.parseString(phrase) );
        } catch (IOException e) {
            r.result = "Exception";
            e.printStackTrace();
        }
        maxPhraseLength = Math.max(maxPhraseLength,r.phrase.length() + 2);
        maxActualLength = Math.max(maxActualLength,r.result.length() + 2);
        maxExpectedLength = Math.max(maxExpectedLength,r.expected.length() + 2);

        results.add(r);
    }
    public void printResults(){
        int failureCnt = 0;
        for (Result r :results){
            String formattedPhrase   = String.format("%-" + maxPhraseLength   + "s"  , "\""+r.phrase   + "\""  );
            String formattedExpected = String.format("%-" + maxExpectedLength + "s"  , "\""+r.expected + "\""  );
            String formattedActual   = String.format("%-" + maxActualLength   + "s"  , "\""+r.result   + "\""  );

            if ( !r.expected.equals( r.result) ){
                System.out.println(String.format("Failure %s:   Expected:%s  Actual:%s ", formattedPhrase, formattedExpected,formattedActual));
                failureCnt++;
            } else{
                System.out.println(String.format("Success %s:   Expected:%s ", formattedPhrase, formattedExpected,formattedActual) );
            }

        }

        if (failureCnt > 0){
            fail( String.format( "There were %s failures", failureCnt));
        }
    }

    @Test
    public void parseFile_whenGivenTestFile_thenAllSymbolsAreTokenized() throws IOException {
        reset();
        validate("word ."               ,          "<sentence><token>word</token><token>.</token></sentence>"                                      );
        validate("word.cat"               ,        "<sentence><token>word.cat</token></sentence>"                     );
        validate("word ..."             ,          "<sentence><token>word</token><token>...</token></sentence>"                 );
        validate("word."                ,           "<sentence><token>word</token><token>.</token></sentence>"                                    );
        validate("word..."              ,          "<sentence><token>word</token><token>...</token></sentence>"                 );
        validate("word,."               ,          "<sentence><token>word</token><token>,</token><token>.</token></sentence>"                   );
        validate("word,..."             ,          "<sentence><token>word</token><token>,</token><token>...</token></sentence>" );
        validate("Euclid's"             ,          "<sentence><token>Euclid's</token></sentence>"                               );
        validate("2.12"                 ,          "<sentence><token>2.12</token></sentence>"                                       );
        validate("$2.12"                 ,         "<sentence><token>$</token><token>2.12</token></sentence>"                                  );
        validate("(21.5"               ,           "<sentence><token>(</token><token>21.5</token></sentence>"                                       );
        validate("20th Century"         ,          "<sentence><token>20th</token><token>Century</token></sentence>"                                       );
        validate("(BFGS)"               ,          "<sentence><token>(</token><token>BFGS</token><token>)</token></sentence>"                                       );

        validate("340x180x90"           ,          "<sentence><token>340x180x90</token></sentence>"                                       );
        validate("340 x 180 x 90"       ,          "<sentence><token>340</token><token>x</token><token>180</token><token>x</token><token>90</token></sentence>"                                       );

        printResults();




    }
    @Test
    public void testPunctuation() throws IOException {
        reset();
        validate("\"Word\""        ,   "<sentence><token>\"</token><token>Word</token><token>\"</token></sentence>"                              );
        validate("\"Word"          ,   "<sentence><token>\"</token><token>Word</token></sentence>"                                               );
        validate("Word\""          ,   "<sentence><token>Word</token><token>\"</token></sentence>"                                               );
        validate("Word,"           ,   "<sentence><token>Word</token><token>,</token></sentence>"                                                );
        validate(",Word"           ,   "<sentence><token>,</token><token>Word</token></sentence>"                                                );
        validate("Word:,"          ,   "<sentence><token>Word</token><token>:</token><token>,</token></sentence>"                                );
        validate("',,,'"           ,   "<sentence><token>'</token><token>,</token><token>,</token><token>,</token><token>'</token></sentence>"   );
        validate("..."             ,   "<sentence><token>...</token></sentence>"                                                                 );
        validate("word.\""         ,   "<sentence><token>word</token><token>.</token><token>.</token></sentence>"                                );
        printResults();

    }

    private void reset() {
        results.clear();
    }

    public void validateSentenceParsing(String str, BasicParser basicParser, int expectedSentences, int expectedTokens) throws IOException {
        System.out.println(String.format ("Testing: '%s'",str));
        
        List<Sentence> sentences = basicParser.parseString(str);
        System.out.println( serializer.serializeSentences(sentences) );

        int totalTokens      = 0;
        for ( Sentence s: sentences){
            totalTokens += s.getTokens().size();
        }
        assertEquals( "Expected Sentences",expectedSentences ,sentences.size()  );
        assertEquals( "Expected Tokens"   ,expectedTokens   ,totalTokens       );
    }


    @Test
    public void parseFile_givenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseString("...");
        assertEquals( sentences.size(),1);
        assertEquals( sentences.get(0).getTokens().size(),1);
    }

    @Test
    public void parseFile_SentenceEndingWithPeriod_ReturnsTwoSentences() throws IOException {
        List<Sentence> sentences = parser.parseString("I am going to the store.  It is hot");

        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );

        assertEquals( sentences.size(),2);
        assertEquals( sentences.get(0).getTokens().size(),7);
        assertEquals( sentences.get(1).getTokens().size(),3);


    }

    @Test
    public void parseFile_whenGivenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseString("...");
        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }
    @Test
    public void parseFile_whenGivenSpaceThenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = parser.parseString(" ...");
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }


}