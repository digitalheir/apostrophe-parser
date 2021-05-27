package com.github.digitalheir.parenthesisparser.category;

import com.github.digitalheir.tokenizer.SubStringWithContext;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;

public class SingleQuoteTerminal implements Terminal<SubStringWithContext> {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return token.obj.str.equals("'");
    }

    @Override
    public String toString() {
        return "Single quote";
    }
}
