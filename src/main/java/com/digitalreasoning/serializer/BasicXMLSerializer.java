package com.digitalreasoning.serializer;

import com.digitalreasoning.entities.File;
import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicXMLSerializer implements XmlSerializer {

    public String serializeSentences(List<Sentence > sentences){
        StringBuilder sb = new StringBuilder();
        if (sentences != null){
            for (Sentence s : sentences){
                serializeSentences(sb,s);
            }
        }

        return  sb.toString();
    }

    public String serializeSentences(StringBuilder sb,List<Sentence > sentences){

        if (sentences != null){
            for (Sentence s : sentences){
                serializeSentences(sb,s);
            }
        }

        return  sb.toString();
    }

    public String serializeFiles(List<File> files){
        StringBuilder sb = new StringBuilder();
        if (files != null){
            for (File f : files){
                if (f.getFileName() != null && f.getFileName() != "" ){
                    sb.append(String.format("<file filename=\"%s\">",f.getFileName()));
                }else {
                    sb.append("<file>\n");
                }
                serializeSentences(sb,f.getSentences() );
                sb.append("</file>\n");
            }
        }

        return  sb.toString();
    }

    private void serializeSentences(StringBuilder sb, Sentence s) {
        if (s != null && s.getTokens() != null){
            sb.append("<sentence>");

            for (Token t : s.getTokens()){
                serializeToken(sb,t);
            }

            sb.append("</sentence>");
        }
    }


    private void serializeToken(StringBuilder sb, Token t) {
        if (t != null && t.getValue() != null){


            if ( t.isNamedEntity){
                sb.append("<token namedEntity=\"true\">");
            } else {
                sb.append("<token>");
            }


            sb.append(t.getValue());


            sb.append("</token>");
        }
    }
}
