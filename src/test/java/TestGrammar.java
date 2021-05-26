import com.github.digitalheir.ParenParser;
import com.github.digitalheir.WordWithContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.leibnizcenter.cfg.token.Token;

import java.util.Arrays;
import java.util.List;

public class TestGrammar {
    @Test
    public void testTokenizer() {
        final List<Token<WordWithContext>> l = ParenParser.tokenize("a.  bCd,");

        final List<Token<WordWithContext>> expected = Arrays.asList(
               new Token<>(new WordWithContext("a", null, null)),
               new Token<>(new WordWithContext(".", null, null)),
               new Token<>(new WordWithContext(" ", null, null)),
               new Token<>(new WordWithContext(" ", null, null)),
               new Token<>(new WordWithContext("bCd", null, null)),
               new Token<>(new WordWithContext(",", null, null))
        );
        for (int i = 0; i < expected.size(); i++) {
            final WordWithContext cur = expected.get(i).obj;
            if (i < expected.size() - 1) {
                cur.next = expected.get(i + 1).obj;
            }
            if (i > 0) {
                cur.prev = expected.get(i - 1).obj;
            }
        }
        Assertions.assertEquals(expected, l);
    }

    @Test
    public void testCases() {
        System.out.println(ParenParser.parse(" 'ok',"));
        System.out.println(ParenParser.parse("'ok'"));
        System.out.println(ParenParser.parse("'ok',"));
    }
}
