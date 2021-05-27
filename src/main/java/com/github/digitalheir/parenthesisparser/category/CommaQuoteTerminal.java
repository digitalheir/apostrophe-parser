package com.github.digitalheir.parenthesisparser.category;

import com.github.digitalheir.tokenizer.SubStringWithContext;
import org.leibnizcenter.cfg.token.Token;

public class CommaQuoteTerminal extends SingleQuoteTerminal {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return super.hasCategory(token) && isComma(token.obj.next);
    }

    private boolean isComma(SubStringWithContext w) {
        return w != null && w.str.equals(",");
    }

    @Override
    public String toString() {
        return super.toString() + " (before comma)";
    }
}
