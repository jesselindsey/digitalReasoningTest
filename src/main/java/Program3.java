import com.digitalreasoning.entities.*;
import com.digitalreasoning.parser.BasicParser;
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
        String testFile = Thread.currentThread().getClass().getResource("/nlp_data.zip").getFile();
        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();


        String outputDirectory = args.length > 1 ? args[1]: System.getProperty("user.dir") + "/output" ;
        File outputFile = new File(outputDirectory);
        if (!outputFile.exists()){
            outputFile.mkdir();
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WorkerThread> threads = new ArrayList<WorkerThread>();
        try {
            ZipFile zipFile = new ZipFile(testFile);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();

                if (!zipEntry.isDirectory() && !zipEntry.getName().startsWith("__MACOSX")) {
                    InputStream is = zipFile.getInputStream(zipEntry);

                    WorkerThread worker = new WorkerThread(is,zipEntry.getName());
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

        List<com.digitalreasoning.entities.File> files = new ArrayList<com.digitalreasoning.entities.File>();
        for ( WorkerThread thread : threads){
            files.add(thread.getOutput());
        }

        System.out.println(serializer.serializeFiles(files));

    }

    private static String generateOutputFileName(String outputDirectory, ZipEntry zipEntry) {
        String[] fileNameParts = zipEntry.getName().split("/");

        return outputDirectory + "/" + fileNameParts[fileNameParts.length-1];
    }

    static class WorkerThread implements Runnable {
        private final String inputFileName;
        InputStream is;

        private com.digitalreasoning.entities.File output;

        public com.digitalreasoning.entities.File getOutput() {
            return output;
        }

        public void setOutput(com.digitalreasoning.entities.File output) {
            this.output = output;
        }

        public WorkerThread(InputStream is, String inputFileName) {
            this.is = is;
            this.inputFileName = inputFileName;
        }

        private BasicParser basicParser = new BasicParser();
        PrettyPrintXMLSerializer serializer = new PrettyPrintXMLSerializer();
        public void run() {
            try {
                List<Sentence> sentences = basicParser.parseStream(is);
                output = new com.digitalreasoning.entities.File(sentences,inputFileName);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
}
