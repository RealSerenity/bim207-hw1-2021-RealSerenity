package edu.estu;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException, CmdLineException {

        Options options = new Options();
        CmdLineParser parser = new CmdLineParser(options);
        parser.parseArgument(args);
        List<String> words = getWords(options.unique, options.fileNames);

        switch (options.task.toLowerCase()) {
            case "numoftokens":
                System.out.println("Number of Tokens:" + words.size());
                break;
            case "frequentterms":
                Map<String, Integer> wordCount = countWords(words);
                printFrequentTerms(wordCount, options.topN);
                break;
        }
    }

    public static List<String> getWords(boolean unique, String... fileNames) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (String fileName : fileNames)
            paths.add(Paths.get(fileName));
        List<String> words = new ArrayList<>();
        for (Path path : paths) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    StringBuilder builder = new StringBuilder();
                    for (char c : token.toCharArray())
                        if (Character.isLetterOrDigit(c))
                            builder.append(Character.toLowerCase(c));
                    String result = builder.toString();
                    if (!result.trim().isEmpty()) words.add(result);
                }
            }
        }
        return unique ? new ArrayList<>(new HashSet<>(words)) : words;
    }

    public static Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> res = new HashMap<>();
        for (String word : words) {
            if (res.containsKey(word))
                res.replace(word, res.get(word) + 1);
            else res.put(word, 1);
        }
        return res;
    }

    public static void printFrequentTerms(Map<String, Integer> map, int topN) {
        for (int i = 0; i < topN; i++) {
            Map.Entry<String, Integer> maxValue = null;
            for (Map.Entry<String, Integer> entry : map.entrySet())
                if (maxValue == null || entry.getValue() > maxValue.getValue())
                    maxValue = entry;
            if (maxValue != null)
                map.remove(maxValue.getKey());
            else return;
            System.out.println(maxValue.getKey() + "   " + maxValue.getValue());
        }
    }
}