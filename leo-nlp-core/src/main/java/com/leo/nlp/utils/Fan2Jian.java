package com.leo.nlp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.jianfan.JianFan;

import java.io.*;

/**
 * Created by lionel on 18/1/30.
 */
@Slf4j
public class Fan2Jian {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.exit(1);
        }
        String inputPath = args[0];
        String outputPath = args[1];
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(new File(inputPath)));
            writer = new BufferedWriter(new FileWriter(new File(outputPath)));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                count += 1;
                if (count % 100 == 0) {
                    log.info("Handle %d line", count);
                }
                writer.write(JianFan.f2j(line) + "\n");
            }
        } catch (IOException e) {
            log.info("IO error", e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
    }
}
