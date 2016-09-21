package com.digitalreasoning.serializer;

import com.digitalreasoning.entities.File;
import com.digitalreasoning.entities.Sentence;

import java.util.List;

/**
 * Created by lindsey on 9/20/16.
 */
public interface XmlSerializer {
    String serializeSentences(List<Sentence > sentences);
    String serializeFiles(List<File> files);
}
