package com.github.digitalheir;

import org.leibnizcenter.cfg.token.Token;

public class SingleParenthesisThatsPartOfAKnownParenthesisPattern extends SingleQuoteTerminal {
    @Override
    public boolean hasCategory(Token<SubStringWithContext> token) {
        return super.hasCategory(token) && token.obj.isPartOfKnownParenthesisPattern();
    }

    @Override
    public String toString() {
        return "Contraction parenthesis";
    }
}
