package com.github.digitalheir.en;

import com.github.digitalheir.SubStringWithContext;
import com.github.digitalheir.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

public class KnownEnglishPatterns {
    static List<List<String>> began = parseResourceFile("began.txt");
    static List<List<String>> ended = parseResourceFile("ended.txt");
    static List<List<String>> inner = parseResourceFile("inner.txt");
    static List<List<String>> outer = parseResourceFile("outer.txt");
    static List<List<String>> verbs = parseResourceFile("verbs.txt");

    static Map<String, List<List<String>>> byPrefix = indexByPrefix(
            began,
            ended,
            inner,
            outer,
            verbs);

    @SafeVarargs
    private static Map<String, List<List<String>>> indexByPrefix(List<List<String>>... patternses) {
        final HashMap<String, List<List<String>>> multimapByPrefix = new HashMap<>(patternses.length * 20);
        for (final List<List<String>> patterns : patternses) {
            for (final List<String> pattern : patterns) {
                final String first = pattern.get(0);
                putInMultiMap(multimapByPrefix, first, pattern);
            }
        }
        for(final List<List<String>> listToOrder : multimapByPrefix.values()) {
            listToOrder.sort(Comparator.comparing(List::size, reverseOrder()));
        }
        return multimapByPrefix;
    }

    private static void putInMultiMap(Map<String, List<List<String>>> byPrefix, String key, List<String> value) {
        final List<List<String>> curList = byPrefix.get(key);
        if (curList != null) {
            curList.add(value);
        } else {
            List<List<String>> newList = new ArrayList<>();
            newList.add(value);
            byPrefix.put(key, newList);
        }
    }

    private static List<List<String>> parseResourceFile(final String filename) {
        try {
            return Objects.requireNonNull(getResourceFileAsString(filename));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static List<List<String>> getResourceFileAsString(String fileName) throws IOException {
        // ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = KnownEnglishPatterns.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (final InputStreamReader isr = new InputStreamReader(is);
                 final BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines()
                        .filter(s -> !s.isBlank())
                        .map(Tokenizer::findAllContiguousSequences)
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * Like lookahead, but we tag the tokens so that we only perform the lookahead once
     */
    public static boolean tagIfIsStartOfKnownPattern(final SubStringWithContext word) {
        final List<List<String>> prefixMatches = byPrefix.get(word.str);
        boolean anyMatch = false;
        if (prefixMatches != null) {
            for (List<String> pattern : prefixMatches) {
                final SubStringWithContext lastPortionOfMatch = matchesPattern(word.next, pattern, 1);
                if (lastPortionOfMatch != null) {
                    word.tagAsStartOfKnownParenthesisPattern(pattern);
                    SubStringWithContext tail = word.next;
                    var ix = 1;
                    while (tail != null) {
                        tail.tagAsTailOfKnownParenthesisPattern(pattern, ix);
                        if (tail == lastPortionOfMatch) tail = null;
                        else tail = tail.next;
                        ix++;
                    }

                    anyMatch = true;

                    // This immediately breaks when we found a match. Because the patterns are ordered by length, this will always be the longest possible match.
                    //  So it is safe to short circuit now.
                    break;
                }
            }
            if (!anyMatch) {
                word.tagAsNoPartOfKnownParenthesisPattern();
            }
        }
        return anyMatch;
    }

    private static SubStringWithContext matchesPattern(SubStringWithContext word, List<String> pattern, int i) {
        final String seqAtIndex = pattern.get(i);
        if (word != null && word.str.equals(seqAtIndex)) {
            // This substring matches!

            if (i >= pattern.size() - 1) {
                // last element to match, return
                return word;
            } else {
                // match next substr
                return matchesPattern(word.next, pattern, i + 1);
            }
        } else {
            // Tokens do not match for this pattern
            return null;
        }
    }
}
