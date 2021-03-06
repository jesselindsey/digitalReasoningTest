package com.digitalreasoning.token;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.serializer.BasicXMLSerializer;
import com.digitalreasoning.serializer.XmlSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicTokenizerSentences_UnitTest {
    BasicTokenizer tokenizer = new BasicTokenizer();
    XmlSerializer serializer = new BasicXMLSerializer();


    @Test
    public void tokenizeFile_givenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = tokenizer.tokenizeString("...");
        assertEquals( sentences.size(),1);
        assertEquals( sentences.get(0).getTokens().size(),1);
    }

    @Test
    public void tokenizeFile_SentenceEndingWithPeriod_ReturnsTwoSentences() throws IOException {
        List<Sentence> sentences = tokenizer.tokenizeString("I am going to the store.  It is hot");

        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );

        assertEquals( sentences.size(),2);
        assertEquals( sentences.get(0).getTokens().size(),7);
        assertEquals( sentences.get(1).getTokens().size(),3);


    }

    @Test
    public void tokenizeFile_whenGivenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = tokenizer.tokenizeString("...");
        System.out.println( new BasicXMLSerializer().serializeSentences(sentences) );
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }
    @Test
    public void tokenizeFile_whenGivenSpaceThenThreeOrMoreRepeatingDots_TokenizeThemTogether() throws IOException {
        List<Sentence> sentences = tokenizer.tokenizeString(" ...");
        assertEquals( 1     ,sentences.size());
        assertEquals( 1     ,sentences.get(0).getTokens().size());
        assertEquals( "..." ,sentences.get(0).getTokens().get(0).getValue());

    }


}