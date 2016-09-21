package com.digitalreasoning.serializer;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public class PrettyPrintXMLSerializer implements XmlSerializer {

    public String serializeSentence(List<Sentence > sentences){
        int currentIndent = 0;
        StringBuilder sb = new StringBuilder();
        if (sentences != null){
            sb.append("<sentences>\n");
            for (Sentence s : sentences){
                serializeSentence(sb,s,currentIndent+1);
            }
            sb.append("</sentences>\n");
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
            sb.append("<token>");

            sb.append(t.getValue());


            sb.append("</token>\n");
        }
    }
}
