package com.github.digitalheir;

import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;

public class AnyTextTerminal implements Terminal<WordWithContext> {
    @Override
    public boolean hasCategory(Token<WordWithContext> token) {
        return true;
    }

    @Override
    public String toString() {
        return "AnyText";
    }
}
