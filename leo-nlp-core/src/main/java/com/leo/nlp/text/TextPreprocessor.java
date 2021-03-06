package com.leo.nlp.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lionel on 17/8/25.
 */
@Slf4j
public class TextPreprocessor {
    private static List<String> stopWords = new ArrayList<String>();

    static {
        loadStopWord("/stop_word.dic");
    }

    private static void loadStopWord(String path) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = TextPreprocessor.class.getResourceAsStream(path);
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException e) {
            log.info("IO error", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(br);
        }
    }

    /**
     * 去掉文本中的常用停用词
     *
     * @param words 文本分完词后存入数组
     * @return 去掉停用词后返回的文本
     */
    public static String[] removeStopWord(String[] words) {
        int length = words.length;
        for (int i = 0; i < words.length; i++) {
            if (stopWords.contains(words[i])) {
                words[i] = null;
                length -= 1;
            }
        }

        String[] array = new String[length];
        int index = 0;
        for (String word : words) {
            if (word == null) {
                continue;
            }
            array[index] = word;
            index += 1;
        }

        return array;
    }

    public static void main(String[] args) {
        String text = "我 的 职业 是 程序猿";
        String[] fields = text.split(" ");
        fields = TextPreprocessor.removeStopWord(fields);
        if (fields != null) {
             for (String ele : fields) {
                System.out.print(ele + " ");
            }
        }
    }
}
