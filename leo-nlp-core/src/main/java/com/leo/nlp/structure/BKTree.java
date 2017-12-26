package com.leo.nlp.structure;

import com.leo.nlp.utils.StringUtil;
import edu.gatech.gtri.bktree.BkTreeSearcher;
import edu.gatech.gtri.bktree.BkTreeSearcher.Match;
import edu.gatech.gtri.bktree.Metric;
import edu.gatech.gtri.bktree.MutableBkTree;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lionel on 17/12/22.
 */
public class BKTree {
    private static MutableBkTree<String> bkTree = null;

    static {
        loadFile("/Users/lionel/Desktop/data/recommendDish/dish.csv");
    }

    private static void loadFile(String path) {
//        Metric<String> editDistance = StringUtils::getLevenshteinDistance;
        Metric<String> editDistance = (x, y) -> {
            String xPinyin = StringUtil.word2Pinyin(x);
            String yPinyin = StringUtil.word2Pinyin(y);
            return StringUtils.getLevenshteinDistance(xPinyin, yPinyin);
        };
        bkTree = new MutableBkTree<>(editDistance);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                bkTree.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> search(String text, int num) {
        List<String> suggest = new ArrayList<>();
        if (StringUtils.isBlank(text)) {
            return suggest;
        }
        BkTreeSearcher<String> searcher = new BkTreeSearcher<>(bkTree);
        Set<Match<? extends String>> matches = searcher.search(text, num);
        if (matches == null) {
            return suggest;
        }
        suggest.addAll(matches.stream().map(Match::getMatch).collect(Collectors.toList()));
        return suggest;
    }
}
