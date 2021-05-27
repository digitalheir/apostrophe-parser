package com.github.digitalheir;

import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;

public class AnyTextTerminal implements Terminal<SubStringWithContext> {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return true;
    }

    @Override
    public String toString() {
        return "literal";
    }
}
