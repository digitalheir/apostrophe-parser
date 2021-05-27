package com.github.digitalheir.tokenizer;

import com.github.digitalheir.SubStringWithContext;
import org.leibnizcenter.cfg.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A token for our purposes is either a contiguous sequence of letters (unicode category L) or any other single character.
 */
public class Tokenizer {
    private static final Pattern tokenPattern = Pattern.compile("\\p{L}+|.", CASE_INSENSITIVE);

    public static List<String> findAllContiguousSequences(String str) {
        final Matcher m = tokenPattern.matcher(str);
        final List<String> allTokens = new ArrayList<>();
        while (m.find()) {
            allTokens.add(m.group());
        }
        return allTokens;
    }

    public static List<Token<SubStringWithContext>> tokenize(String str) {
        final List<String> allTokens = findAllContiguousSequences(str);
        final List<Token<SubStringWithContext>> myTokens = new ArrayList<>(allTokens.size());
        SubStringWithContext lastWord = null;
        for (final String word : allTokens) {
            final SubStringWithContext subStringWithContext = new SubStringWithContext(word, lastWord, null);
            myTokens.add(new Token<>(subStringWithContext));
            if (lastWord != null) lastWord.next = subStringWithContext;
            lastWord = subStringWithContext;
        }

        return myTokens;
    }

    public static List<SubStringWithContext> analyze(String str) {
        final List<String> allTokens = findAllContiguousSequences(str);
        final List<SubStringWithContext> myTokens = new ArrayList<>(allTokens.size());
        SubStringWithContext lastWord = null;
        for (final String word : allTokens) {
            final SubStringWithContext subStringWithContext = new SubStringWithContext(word, lastWord, null);
            myTokens.add(subStringWithContext);
            if (lastWord != null) lastWord.next = subStringWithContext;
            lastWord = subStringWithContext;
        }
        return myTokens;
    }
}