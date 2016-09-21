package com.digitalreasoning.serializer;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class BasicXMLSerializer {

    public String serializeSentence(List<Sentence > sentences){
        StringBuilder sb = new StringBuilder();
        if (sentences != null){
            for (Sentence s : sentences){
                serializeSentence(sb,s);
            }
        }

        return  sb.toString();
    }

    private void serializeSentence(StringBuilder sb, Sentence s) {
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
            sb.append("<token>");
            sb.append(t.getValue());
            sb.append("</sentence>");
        }
    }
}
