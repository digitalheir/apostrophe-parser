package com.github.digitalheir.tokenizer;

import com.github.digitalheir.en.KnownPatternTag;

import java.util.List;
import java.util.Objects;

import static com.github.digitalheir.en.KnownEnglishPatterns.tagIfIsStartOfKnownPattern;

public class SubStringWithContext {
    public final String str;
    public SubStringWithContext prev;
    public SubStringWithContext next;
    /**
     * null                   : Undetermined
     * KnownPatternTag.ix <  0: Not a part of any known parenthesis pattern
     * KnownPatternTag.ix == 0: Start of pattern
     * KnownPatternTag.ix >  0: Tail of pattern
     */
    public KnownPatternTag partOfKnownParenthesisPattern = null;

    public SubStringWithContext(String str, SubStringWithContext prev, SubStringWithContext next) {
        this.str = str;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || getClass() != o.getClass()) return false;
        else {
            SubStringWithContext that = (SubStringWithContext) o;
            return Objects.equals(str, that.str) && shallowEquals(prev, that.prev) && shallowEquals(next, that.next);
        }
    }

    private boolean shallowEquals(SubStringWithContext one, SubStringWithContext other) {
        return one == null ? other == null : other != null && Objects.equals(one.str, other.str);
    }

    @Override
    public int hashCode() {
        final SubStringWithContext p = this.prev;
        final SubStringWithContext n = this.next;
        return Objects.hash(str, p != null ? p.str : null, n != null ? n.str : null);
    }

    @Override
    public String toString() {
        final SubStringWithContext p = this.prev;
        final SubStringWithContext n = this.next;
        final String strInContext = (p != null ? p.str : "") + "[" + str + "]" + (n != null ? n.str : "");
        final KnownPatternTag matchedPattern = partOfKnownParenthesisPattern;
        if (matchedPattern != null && matchedPattern.ix >= 0) {
            final List<String> pattern = matchedPattern.pattern;
            final String joined = String.join("", pattern);
            return strInContext + ", part of known pattern ‹" + joined + "›";
        } else {
            return strInContext;
        }
    }

    public void tagAsNoPartOfKnownParenthesisPattern() {
        partOfKnownParenthesisPattern = KnownPatternTag.NO_PATTERN_MATCHES;
    }

    public void tagAsStartOfKnownParenthesisPattern(final List<String> pattern) {
        partOfKnownParenthesisPattern = new KnownPatternTag(pattern, 0);
    }

    public void tagAsTailOfKnownParenthesisPattern(final List<String> pattern, int ix) {
        partOfKnownParenthesisPattern = new KnownPatternTag(pattern, ix);
    }

    public boolean isPartOfKnownParenthesisPattern() {
        KnownPatternTag knownPattern = this.partOfKnownParenthesisPattern;
        if (knownPattern != null) {
            return knownPattern.ix >= 0;
        } else {
            return tagIfIsStartOfKnownPattern(this);
        }
    }

    public void tagIfNotAlreadyTagged() {
        final KnownPatternTag knownPattern = this.partOfKnownParenthesisPattern;
        if (knownPattern == null) {
            // only tag if not already tagged (this will also tag the matching sequences that follow)
            tagIfIsStartOfKnownPattern(this);
        }
    }
}
