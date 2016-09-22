package com.digitalreasoning.token;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;
import com.digitalreasoning.serializer.BasicXMLSerializer;
import com.digitalreasoning.serializer.XmlSerializer;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by lindsey on 9/21/16.
 */
public class NamedEntityPostProcessorTest {
    XmlSerializer serializer = new BasicXMLSerializer();
    @Test
    public void test1() throws IOException {
        String testFile = Thread.currentThread().getClass().getResource("/NER.txt").getFile();
        NamedEntityPostProcessor namedEntityPostProcessor = new NamedEntityPostProcessor((testFile));
        Sentence testSentence = new Sentence();
        testSentence.getTokens().add( new Token("Ernst"));
        testSentence.getTokens().add( new Token("Haeckel"));
        Sentence result = namedEntityPostProcessor.consolidate(testSentence);
        System.out.println(serializer.serializeSentences(Arrays.asList(result)));
        assertEquals( 1, result.getTokens().size()  );
        assertTrue(  result.getTokens().get(0).isNamedEntity()  );
    }

    @Test
    public void test2() throws IOException {
        String testFile = Thread.currentThread().getClass().getResource("/NER.txt").getFile();
        NamedEntityPostProcessor namedEntityPostProcessor = new NamedEntityPostProcessor((testFile));
        Sentence testSentence = new Sentence();
        testSentence.getTokens().add( new Token("Ernst"));
        testSentence.getTokens().add( new Token("Haeckel"));
        testSentence.getTokens().add( new Token("unrelated"));
        Sentence result = namedEntityPostProcessor.consolidate(testSentence);

        System.out.println(serializer.serializeSentences(Arrays.asList(result)));
        assertEquals( 2, result.getTokens().size()  );
        assertTrue(  result.getTokens().get(0).isNamedEntity()  );
    }

}