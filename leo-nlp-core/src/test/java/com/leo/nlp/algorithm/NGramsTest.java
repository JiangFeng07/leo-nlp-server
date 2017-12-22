package com.leo.nlp.algorithm;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lionel on 17/12/22.
 */
public class NGramsTest {
    @Test
    public void test() {
        NGrams ng = new NGrams();
        int N = 3;
        ng.loadModel("/Users/lionel/Desktop/srilm/LM");
        List<Double> prob = ng.predictLogProb(StringUtils.join(Arrays.asList("红烧肉".split("")), " "), N);
        System.out.println(prob);
        System.out.println(ng.getPerplexity(StringUtils.join(Arrays.asList("红烧肉".split("")), " ")));
    }

//    @Test
//    public void test2() {
//        NGrams ng = new NGrams();
//        ng.loadModel("/Users/lionel/Desktop/srilm/LM");
//        int N = 3;
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(new File("/Users/lionel/Desktop/data/recommendDish/dish.csv")));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/tmp/1.csv")));
//            String line = reader.readLine();
//            while ((line = reader.readLine()) != null) {
//                String tmp = StringUtils.join(Arrays.asList(line.split("")), " ");
//                double ppl = ng.getPerplexity(ng.predictLogProb(tmp, N));
//                if (ppl > 100) {
//                    continue;
//                }
//                writer.write(line + "\n");
//
//                String[] dishes = line.trim().split("\\t");
//                if (dishes.length != 2) {
//                    continue;
//                }
//                double ppl = ng.getPerplexity(ng.predictLogProb(StringUtils.join(Arrays.asList(dishes[0].split("")), " "), N));
//                double ppl2 = ng.getPerplexity(ng.predictLogProb(StringUtils.join(Arrays.asList(dishes[1].split("")), " "), N));
//                if (ppl > ppl2) {
//                    continue;
//                }
//                System.out.println(String.format("%s:%f <<-->> %s:%f", dishes[0], ppl, dishes[1], ppl2));
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
