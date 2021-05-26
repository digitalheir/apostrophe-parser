package com.github.digitalheir;

import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;

public class SingleQuoteTerminal implements Terminal<WordWithContext> {
    @Override
    public boolean hasCategory(Token<WordWithContext> token) {
        return token.obj.word.equals("'");
    }

    @Override
    public String toString() {
        return "Single quote";
    }
}
