import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.parser.BasicParser;
import com.digitalreasoning.serializer.PrettyPrintXMLSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class Program1 {
    public static void main(String[] args) throws IOException {
        String testFile = Thread.currentThread().getClass().getResource("/nlp_data.txt").getFile();

        BasicParser parser = new BasicParser(  );
        List<Sentence> sentences = parser.parseFile(new FileInputStream(testFile));

        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();
        System.out.println(serializer.serializeSentences(sentences));
    }
}
