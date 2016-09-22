package com.digitalreasoning.token;

import com.digitalreasoning.entities.Sentence;
import com.digitalreasoning.entities.Token;

import java.io.*;
import java.util.*;

/**
 * Created by lindsey on 9/20/16.
 */
public class NamedEntityPostProcessor {

    HashMap<String,List<String[]>> names ;
    public NamedEntityPostProcessor(String filename) throws IOException {

        names = createMapOfNamedEntities(filename);

    }

    private HashMap<String, List<String[]>> createMapOfNamedEntities(String filename) throws IOException {
        HashMap<String,List<String[]>> names = new HashMap<String,List<String[]>>();

        BufferedReader br=null;
        try{
            String thisLine=null;
            br = new BufferedReader( new FileReader( filename ));

            while ((thisLine = br.readLine()) != null) {
                if (thisLine.length() > 0) {
                    String[] nameArray = thisLine.split("\\s+");
                    if (!names.containsKey(nameArray[0])){
                        names.put(nameArray[0],new ArrayList() );
                    }
                    names.get(nameArray[0]).add(nameArray);


                }
            }

        }finally{
            if (br != null){
                br.close();
            }
        }
        return names;
    }

    public Sentence consolidate(Sentence sentence){
        Sentence consolidated = sentence;
        for (int i=0; i < sentence.getTokens().size();i++){

            String[] match = matchNamedEntity( sentence.getTokens(),i );

            if ( match != null ){
                // create a new consolidated token
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : match){
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(s);
                }
                Token token = new Token(stringBuilder.toString() );
                token.setNamedEntity(true);
                consolidated.getTokens().add( i,token );

                // remove other tokens
                for ( int n = 0; n < match.length; n++){
                    consolidated.getTokens().remove( i+1 );
                }



            }
        }
        return sentence;
    }

    protected String[] matchNamedEntity(List<Token> tokens, int i) {
        Token token = tokens.get(i);
        List<String[]> list = names.get(token.getValue());
        if ( list !=null){
            for (String [] sa: list) {
                boolean isMatch=true;
                for (int li = 0; li < sa.length; li++) {
                    if ( i + li >= tokens.size()  || !tokens.get(i + li).getValue().equals(sa[li])  ){
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch){
                    return sa;
                }
            }

        }
        return null;
    }



}
