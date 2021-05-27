package com.github.digitalheir;

import org.leibnizcenter.cfg.token.Token;

public class SpaceQuoteTerminal extends SingleQuoteTerminal {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return super.hasCategory(token) && isSpace(token.obj.prev);
    }

    private boolean isSpace(SubStringWithContext w) {
        return w != null && w.str.equals(" ");
    }

    @Override
    public String toString() {
        return super.toString() + " (after space)";
    }
}
