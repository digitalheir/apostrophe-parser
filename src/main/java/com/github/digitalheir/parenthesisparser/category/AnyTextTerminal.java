package com.github.digitalheir.parenthesisparser.category;

import com.github.digitalheir.tokenizer.SubStringWithContext;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;

public class AnyTextTerminal implements Terminal<SubStringWithContext> {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return true; // catch-all
    }

    @Override
    public String toString() {
        return "literal";
    }
}
