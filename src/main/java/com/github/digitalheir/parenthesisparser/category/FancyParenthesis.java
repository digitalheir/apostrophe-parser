package com.github.digitalheir.parenthesisparser.category;

import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;

public class FancyParenthesis extends NonTerminal {
    public final String replacement;

    public FancyParenthesis(final String replacement, final String name) {
        super(name);
        this.replacement = replacement;
    }
}