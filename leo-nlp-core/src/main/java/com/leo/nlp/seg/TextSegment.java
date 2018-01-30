package com.leo.nlp.seg;

import lombok.extern.slf4j.Slf4j;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.AmbiguityLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lionel on 17/9/12.
 */
@Slf4j
public class TextSegment {
    //词库
    public static final Forest forest = new Forest();

    // 各种过滤器
    public static final StopRecognition stopNull = new StopRecognition();
    public static final StopRecognition stopWord = new StopRecognition();

    // 自定义分词器
    private static final DicAnalysis dishAnalysis = new DicAnalysis();


    static {
        AmbiguityLibrary.put(AmbiguityLibrary.DEFAULT, AmbiguityLibrary.DEFAULT, new Forest());
        makeForest(forest, "/Users/lionel/Downloads/dish.txt", "dish");
        stopNull.insertStopNatures("null");
        stopNull.insertStopNatures("w");
        loadStopWord(stopWord, "/stop_word.dic");
        dishAnalysis.setForests(forest);
    }

    private static void loadStopWord(StopRecognition stopRecognition, String dic) {
        log.info(String.format("Load %s to %s", dic, stopRecognition));
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = TextSegment.class.getResourceAsStream(dic);
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String token = line.replaceAll("[\\r\\n]", "");
                if (StringUtils.isBlank(token)) {
                    continue;
                }
                stopWord.insertStopWords(token.toLowerCase().trim());
            }
        } catch (IOException e) {
            log.error("IO error.", e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    private static void makeForest(Forest forest, String dic, String nature) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dic)));
            String line;
            while ((line = reader.readLine()) != null) {
                Library.insertWord(forest, new Value(line.toLowerCase().trim(), nature, "10000"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static synchronized Result parseForDishName(String text) {
        if (StringUtils.isBlank(text)) {
            return new Result(new ArrayList<Term>());
        }
        return dishAnalysis.parseStr(text.toLowerCase().trim()).recognition(stopNull).recognition(stopWord);
    }


    public static String[] parse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        Result result = ToAnalysis.parse(text);
        int length = result.getTerms().size();
        String[] words = new String[length];
        for (int i = 0; i < length; i++) {
            String name = result.getTerms().get(i).getName();
            words[i] = name;
        }
        return words;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.exit(1);
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(new File(args[0])));
            writer = new BufferedWriter(new FileWriter(new File(args[1])));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("\\t");
                if (fields.length != 2) {
                    continue;
                }
                Result result = TextSegment.parseForDishName(fields[1]);
                List<String> words = result.getTerms().stream()
                        .filter(e -> !e.getNatureStr().equals("null"))
                        .filter(e -> !e.getNatureStr().startsWith("w"))
                        .filter(e -> !e.getNatureStr().startsWith("t"))
                        .map(Term::getName).collect(Collectors.toList());
                writer.write(String.format("%s %s\n", fields[0], StringUtils.join(words, ",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
    }
}
