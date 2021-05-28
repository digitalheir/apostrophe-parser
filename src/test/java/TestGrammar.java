import com.github.digitalheir.parenthesisparser.ParenthesisParser;
import com.github.digitalheir.tokenizer.SubStringWithContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.leibnizcenter.cfg.token.Token;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.digitalheir.tokenizer.Tokenizer.tokenize;

public class TestGrammar {
  @Test
  public void testTokenizer() {
    final List<Token<SubStringWithContext>> l = tokenize( "a.  bCd," );

    final List<Token<SubStringWithContext>> expected = Arrays.asList(
      new Token<>( new SubStringWithContext( "a", null, null ) ),
      new Token<>( new SubStringWithContext( ".", null, null ) ),
      new Token<>( new SubStringWithContext( " ", null, null ) ),
      new Token<>( new SubStringWithContext( " ", null, null ) ),
      new Token<>( new SubStringWithContext( "bCd", null, null ) ),
      new Token<>( new SubStringWithContext( ",", null, null ) )
    );
    for( int i = 0; i < expected.size(); i++ ) {
      final SubStringWithContext cur = expected.get( i ).obj;
      if( i < expected.size() - 1 ) {
        cur.next = expected.get( i + 1 ).obj;
      }
      if( i > 0 ) {
        cur.prev = expected.get( i - 1 ).obj;
      }
    }
    Assertions.assertEquals( expected, l );
  }

  @Test
  public void convertToFancyQuotes() {
    Assertions.assertEquals(
      "‘Hello, I'm here’, she said.",
      ParenthesisParser.convertToFancyQuotes( "'Hello, I'm here', she said." )
    );
  }

  @Test
  public void testCases() {
    System.out.println( ParenthesisParser.parse( "ain't it funny" ) );
    System.out.println( ParenthesisParser.parse( "ain't it funny'" ) );
    System.out.println( ParenthesisParser.parse( "'ok'" ) );
    System.out.println( ParenthesisParser.parse( "'ok'," ) );
    System.out.println( ParenthesisParser.parse( " 'ok'," ) );
  }

  @Test
  public void test_Conversion_InputSuite_AllConverted() throws IOException {
    final var qc = new QuoteConverter( "smartypants.txt" );
    qc.convert( ParenthesisParser::convertToFancyQuotes );
  }
}
