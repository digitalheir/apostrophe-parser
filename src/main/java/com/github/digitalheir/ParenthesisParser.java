package com.github.digitalheir;

import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.earleyparser.ParseTree;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.grammar.Grammar;
import org.leibnizcenter.cfg.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParenthesisParser {
    final static NonTerminal sentence = NonTerminal.of("S");
    final static NonTerminal openingQuote = NonTerminal.of("['");
    final static NonTerminal closingQuote = NonTerminal.of("']");

    final static Terminal<WordWithContext> anySingleQuote = new SingleQuoteTerminal();
    final static Terminal<WordWithContext> singleQuoteAfterSpace = new SpaceQuoteTerminal();
    final static Terminal<WordWithContext> singleQuoteBeforeComma = new CommaQuoteTerminal();

    final static AnyTextTerminal textLiteral = new AnyTextTerminal();

    final static Grammar<WordWithContext> gram = new Grammar.Builder<WordWithContext>()
            .addRule(0.999, sentence,
                    openingQuote, sentence, closingQuote)
            .addRule(0.00099, sentence,
                    sentence, sentence)
            .addRule(0.00001, sentence,
                    textLiteral)

            // Rules for opening
            .addRule(0.4, openingQuote,
                    anySingleQuote)
            .addRule(0.6, openingQuote,
                    singleQuoteAfterSpace)

            // Rules for closing
            .addRule(0.4, closingQuote,
                    anySingleQuote)
            .addRule(0.6, closingQuote,
                    singleQuoteBeforeComma)

            .build();

    private static final Pattern tokenPattern = Pattern.compile("[a-z]+|.", Pattern.CASE_INSENSITIVE);

    public static List<Token<WordWithContext>> tokenize(String str) {
        final Matcher m = tokenPattern.matcher(str);
        final List<String> allTokens = new ArrayList<>();
        while (m.find()) {
            allTokens.add(m.group());
        }


        final List<Token<WordWithContext>> myTokens = new ArrayList<>(allTokens.size());
        WordWithContext lastWord = null;
        for (final String word : allTokens) {
            final WordWithContext wordWithContext = new WordWithContext(word, lastWord, null);
            myTokens.add(new Token<>(wordWithContext));
            if (lastWord != null) lastWord.next = wordWithContext;
            lastWord = wordWithContext;
        }

        return myTokens;
    }

    public static ParseTree parse(String str) {
        return new Parser<>(gram).getViterbiParse(sentence, tokenize(str));
    }

}
