package com.github.digitalheir;

import org.leibnizcenter.cfg.token.Token;

public class CommaQuoteTerminal extends SingleQuoteTerminal {
    @Override
    public boolean hasCategory(Token<WordWithContext> token) {
        return super.hasCategory(token) && isComma(token.obj.next);
    }

    private boolean isComma(WordWithContext w) {
        return w != null && w.word.equals(",");
    }

    @Override
    public String toString() {
        return super.toString() + " (before comma)";
    }
}
