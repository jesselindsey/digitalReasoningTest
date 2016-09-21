package com.digitalreasoning.serializer;

import com.digitalreasoning.entities.File;
import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class PrettyPrintXMLSerializer implements XmlSerializer {

    public String serializeSentences(List<Sentence > sentences){

        StringBuilder sb = new StringBuilder();
        return serializeSentences(sb,sentences,0);
    }
    public String serializeSentences(  StringBuilder sb ,List<Sentence > sentences,   int currentIndent){
        if (sentences != null){
            addIndent(sb,currentIndent);
            sb.append("<sentences>\n");
            for (Sentence s : sentences){
                serializeSentence(sb,s,currentIndent+1);
            }
            addIndent(sb,currentIndent);
            sb.append("</sentences>\n");
        }

        return  sb.toString();
    }

    public String serializeFiles(List<File> files){
        StringBuilder sb = new StringBuilder();
        if (files != null){
            for (File f : files){
                if (f.getFileName() != null && f.getFileName() != "" ){
                    sb.append(String.format("<file filename=\"%s\">\n",f.getFileName()));
                }else {
                    sb.append("<file>\n");
                }
                serializeSentences(sb,f.getSentences() , 1);
                sb.append("</file>\n");
            }
        }

        return  sb.toString();
    }

    private void addIndent(StringBuilder sb,int indent){
        for (int i = 0 ; i < indent; i++){
            sb.append("  ");
        }
    }
    private void serializeSentence(StringBuilder sb, Sentence s, int currentIndent) {
        if (s != null && s.getTokens() != null){
            addIndent(sb,currentIndent);
            sb.append("<sentence>\n");

            for (Token t : s.getTokens()){
                serializeToken(sb,t,currentIndent+1);
            }

            addIndent(sb,currentIndent);
            sb.append("</sentence>\n");
        }
    }

    private void serializeToken(StringBuilder sb, Token t, int currentIndent) {
        if (t != null && t.getValue() != null){
            addIndent(sb,currentIndent);

            if ( t.isNamedEntity){
                sb.append("<token namedEntity=\"true\">");
            } else {
                sb.append("<token>");
            }


            sb.append(t.getValue());


            sb.append("</token>\n");
        }
    }
}
