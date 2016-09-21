import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.parser.BasicParser;
import com.digitalreasoning.parser.NamedEntityPostProcessor;
import com.digitalreasoning.serializer.PrettyPrintXMLSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lindsey on 9/21/16.
 */
public class Program2 {
    public static void main(String[] args) throws IOException {
        InputStream testFileStream = Thread.currentThread().getClass().getResourceAsStream("/nlp_data.txt");

        String namedEntityFile = Thread.currentThread().getClass().getResource("/NER.txt").getFile();
        NamedEntityPostProcessor namedEntityPostProcessor = new NamedEntityPostProcessor((namedEntityFile));

        BasicParser parser = new BasicParser();

        List<Sentence> sentences = parser.parseStream(testFileStream);
        for (Sentence s: sentences){
            namedEntityPostProcessor.consolidate(s);
        }

        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();
        System.out.println(serializer.serializeSentences(sentences));

        testFileStream.close();
    }
}
