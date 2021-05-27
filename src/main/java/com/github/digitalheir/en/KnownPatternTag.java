package com.github.digitalheir.en;

import java.util.List;

public class KnownPatternTag {
    public static final KnownPatternTag NO_PATTERN_MATCHES = new KnownPatternTag(null, -1);

    public final List<String> pattern;
    public final int ix;

    /**
     * @param pattern pattern that was matched for the token this object belongs to
     * @param ix index of the pattern this object belongs to, < 0 means no match was found
     */
    public KnownPatternTag(List<String> pattern, int ix) {
        this.pattern = pattern;
        this.ix = ix;
    }
}
