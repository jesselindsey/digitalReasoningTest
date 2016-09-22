import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.token.BasicTokenizer;
import com.digitalreasoning.serializer.PrettyPrintXMLSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class Program1 {
    public static void main(String[] args) throws IOException {
        InputStream testFileStream = Thread.currentThread().getClass().getResourceAsStream("/nlp_data.txt");

        BasicTokenizer tokenizer = new BasicTokenizer(  );
        List<Sentence> sentences = tokenizer.tokenizeStream(testFileStream);

        testFileStream.close();

        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();
        System.out.println(serializer.serializeSentences(sentences));
    }
}
