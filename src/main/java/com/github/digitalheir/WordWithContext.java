package com.github.digitalheir;

import java.util.Objects;

public class WordWithContext {
    final String word;
    public WordWithContext prev;
    public WordWithContext next;

    public WordWithContext(String word, WordWithContext prev, WordWithContext next) {
        this.word = word;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || getClass() != o.getClass()) return false;
        else {
            WordWithContext that = (WordWithContext) o;
            return Objects.equals(word, that.word) && shallowEquals(prev, that.prev) && shallowEquals(next, that.next);
        }
    }

    private boolean shallowEquals(WordWithContext one, WordWithContext other) {
        return one == null ? other == null : other != null && Objects.equals(one.word, other.word);
    }

    @Override
    public int hashCode() {
        final WordWithContext p = this.prev;
        final WordWithContext n = this.next;
        return Objects.hash(word, p != null ? p.word : null, n != null ? n.word : null);
    }

    @Override
    public String toString() {
        final WordWithContext p = this.prev;
        final WordWithContext n = this.next;
        return (p != null ? p.word  : "")+ "[" + word + "]" +(n != null ?  n.word  : "");
    }
}
