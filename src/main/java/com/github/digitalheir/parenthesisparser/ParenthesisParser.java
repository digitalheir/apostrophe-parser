package com.github.digitalheir.parenthesisparser;

import com.github.digitalheir.parenthesisparser.category.*;
import com.github.digitalheir.tokenizer.SubStringWithContext;
import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.earleyparser.ParseTree;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.grammar.Grammar;
import org.leibnizcenter.cfg.token.Token;

import java.util.List;

import static com.github.digitalheir.tokenizer.Tokenizer.tokenize;

public class ParenthesisParser {
    final static NonTerminal sentence = NonTerminal.of("S");
    final static NonTerminal openingQuote = NonTerminal.of("Opening quote");
    final static NonTerminal closingQuote = NonTerminal.of("Closing quote");

    final static Terminal<SubStringWithContext> anySingleQuote = new SingleQuoteTerminal();
    final static Terminal<SubStringWithContext> singleQuoteAfterSpace = new SpaceQuoteTerminal();
    final static Terminal<SubStringWithContext> singleQuoteBeforeComma = new CommaQuoteTerminal();

    final static Terminal<SubStringWithContext> singleParenthesisPartOfKnownPattern = new SingleParenthesisThatsPartOfAKnownParenthesisPattern();

    final static AnyTextTerminal textLiteral = new AnyTextTerminal();

    // Take care that all probabilities for Non-Terminals should add up to <= 1.0!
    final static Grammar<SubStringWithContext> gram = new Grammar.Builder<SubStringWithContext>()
            // Opening and closing parenthesis is kind of salient, so the probability should be a little high
            .addRule(0.399, sentence,
                    openingQuote, sentence, closingQuote)

            // These probabilities can be very low, because they are sort of fallback
            .addRule(0.00099, sentence,
                    sentence, sentence)
            .addRule(0.00001, sentence,
                    textLiteral)

            // Known contractions are very likely to not be a opening or closing quote
            .addRule(0.6, sentence,
                    singleParenthesisPartOfKnownPattern)


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


    public static ParseTree parse(String str) {
        final List<Token<SubStringWithContext>> tokens = tokenizeAndPreprocess(str);
        return new Parser<>(gram).getViterbiParse(sentence, tokens);
    }

    private static List<Token<SubStringWithContext>> tokenizeAndPreprocess(String str) {
        final List<Token<SubStringWithContext>> tokens = tokenize(str);
        for (final Token<SubStringWithContext> token : tokens) {
            token.obj.tagIfNotAlreadyTagged();
        }
        return tokens;
    }

    public static String convertToFancyQuotes(String str) {
        final ParseTree parseTree = parse(str);
        final StringBuilder sb = new StringBuilder(str.length());
        convertToFancyQuotes(parseTree, sb);
        return sb.toString();
    }

    /**
     * Walk through the tree, converting parsed quotes ('') to fancy quotes (‘’), while leaving the other text intact
     */
    private static void convertToFancyQuotes(ParseTree parseTree, StringBuilder sb) {
        if (parseTree instanceof ParseTree.Token) {
            // We've hit a leaf node; emit the literal text to the StringBuilder
            final String literal = ((SubStringWithContext) ((ParseTree.Token<?>) parseTree).token.obj).str;
            sb.append(literal);
        } else {
            // We're on a NonTerminal; maybe we should emit a quote
            final Category category = parseTree.category;
            if (category == openingQuote) {
                sb.append("‘");
            } else if (category == closingQuote) {
                sb.append("’");
            } else {
                // If we're on a non-interesting NonTerminal; recurse into children
                final List<ParseTree> children = parseTree.children;
                if (children != null) for (ParseTree childNode : children) {
                    convertToFancyQuotes(childNode, sb);
                }
            }
        }
    }
}