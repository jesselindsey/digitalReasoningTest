package com.digitalreasoning.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindsey on 9/21/16.
 */
public class File{
    List<Sentence> sentences = new ArrayList<Sentence>();
    String fileName ;

    public File(List<Sentence> sentences, String fileName) {
        this.sentences = sentences;
        this.fileName = fileName;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
