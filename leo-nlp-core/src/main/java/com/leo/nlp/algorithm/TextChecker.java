package com.leo.nlp.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lionel on 17/8/22.
 */
@Slf4j
public class TextChecker {
    private static final int SUGGEST_SIZE = 10;
    private static SpellChecker spellChecker = null;

    static {
        createIndex("/minreng.dic");
    }

    private static void createIndex(String dicPath) {
        log.info("begin create index...");
        InputStream inputStream = null;
        try {
            //索引存储在内存中
            Directory directory = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            spellChecker = new SpellChecker(directory);
            inputStream = TextChecker.class.getResourceAsStream(dicPath);
            spellChecker.indexDictionary(new PlainTextDictionary(inputStream), writerConfig, false);
        } catch (IOException e) {
            log.error("IO error", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static String suggest(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        String[] suggestions = new String[SUGGEST_SIZE];
        try {
            suggestions = spellChecker.suggestSimilar(text, SUGGEST_SIZE);
        } catch (IOException e) {
            log.error("IO error", e);
        }
        if (suggestions.length <= 0) {
            return "";
        }
        return suggestions[0];
    }
}
