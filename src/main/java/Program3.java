import com.digitalreasoning.entities.*;
import com.digitalreasoning.token.BasicTokenizer;
import com.digitalreasoning.serializer.PrettyPrintXMLSerializer;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by lindsey on 9/21/16.
 */
public class Program3 {
    public static void main(String[] args){
        String zipFilePath = Thread.currentThread().getClass().getResource("/nlp_data.zip").getFile();

        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();


        // determine and setup output directory
        String outputDirectory = args.length > 1 ? args[1]: System.getProperty("user.dir") + "/output" ;
        File outputFile = new File(outputDirectory);
        if (!outputFile.exists()){
            outputFile.mkdir();
        }

        // setup threadpool
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<TokenizerThread> threads = new ArrayList<TokenizerThread>();


        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                //  Find text files exclude system files
                if (!zipEntry.isDirectory() && !zipEntry.getName().startsWith("__MACOSX") && zipEntry.getName().endsWith(".txt")) {
                    InputStream is = zipFile.getInputStream(zipEntry);

                    TokenizerThread worker = new TokenizerThread(is,zipEntry.getName());
                    threads.add(worker);
                    executor.execute(worker);


                }

            }

            executor.shutdown();
            while (!executor.isTerminated()) {   }
            zipFile.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        // loop through threads and aggregate the results
        List<com.digitalreasoning.entities.File> files = new ArrayList<com.digitalreasoning.entities.File>();
        for ( TokenizerThread thread : threads){
            files.add(thread.getOutput());
        }

        System.out.println(serializer.serializeFiles(files));

    }


    static class TokenizerThread implements Runnable {
        private final String inputFileName;
        InputStream is;

        private com.digitalreasoning.entities.File output;

        public com.digitalreasoning.entities.File getOutput() {
            return output;
        }

        public TokenizerThread(InputStream is, String inputFileName) {
            this.is = is;
            this.inputFileName = inputFileName;
        }

        private BasicTokenizer basicTokenizer = new BasicTokenizer();
        public void run() {
            try {
                List<Sentence> sentences = basicTokenizer.tokenizeStream(is);
                output = new com.digitalreasoning.entities.File(sentences,inputFileName);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
}
