package com.github.digitalheir;

import org.leibnizcenter.cfg.token.Token;

public class SpaceQuoteTerminal extends SingleQuoteTerminal {
    @Override
    public boolean hasCategory(Token<WordWithContext> token) {
        return super.hasCategory(token) && isSpace(token.obj.prev);
    }

    private boolean isSpace(WordWithContext w) {
        return w != null && w.word.equals(" ");
    }

    @Override
    public String toString() {
        return super.toString() + " (after space)";
    }
}
