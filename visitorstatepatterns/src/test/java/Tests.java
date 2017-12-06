import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import token.Token;
import visitors.CalcVisitor;
import visitors.ParserVisitor;

import java.util.List;
import java.util.Stack;

public class Tests {

    private Tokenizer tokenizer = new Tokenizer();

    private ParserVisitor parserVisitor = new ParserVisitor();

    private CalcVisitor calcVisitor = new CalcVisitor();


    @Before
    public void settingVisitors() {
        tokenizer = new Tokenizer();
        parserVisitor = new ParserVisitor();
        calcVisitor = new CalcVisitor();
    }

    @Test
    public void parse1() {
        String expr = "1 * 2 * (3 + 4) - 5";
        List<Token> tokens = tokenizer.getTokens(expr);
        Stack<Token> rpn = parserVisitor.parse(tokens);
        int calculate = calcVisitor.calculate(rpn);

        Assert.assertEquals(9, calculate);
    }

    @Test
    public void parse2() {
        String expr = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2";
        List<Token> tokens = tokenizer.getTokens(expr);
        Stack<Token> rpn = parserVisitor.parse(tokens);
        int calculate = calcVisitor.calculate(rpn);

        Assert.assertEquals(1279, calculate);
    }


    @Test(expected = IllegalStateException.class)
    public void parse3() {
        String expr = "2 3 +";
        List<Token> tokens = tokenizer.getTokens(expr);
        Stack<Token> rpn = parserVisitor.parse(tokens);
        int calculate = calcVisitor.calculate(rpn);
    }


    @Test(expected = IllegalStateException.class)
    public void parse4() {
        String expr = "2 + + 4";
        List<Token> tokens = tokenizer.getTokens(expr);
        Stack<Token> rpn = parserVisitor.parse(tokens);
        calcVisitor.calculate(rpn);
    }


    @Test(expected = IllegalStateException.class)
    public void parse5() {
        String expr = "-1";
        List<Token> tokens = tokenizer.getTokens(expr);
        Stack<Token> rpn = parserVisitor.parse(tokens);
        calcVisitor.calculate(rpn);
    }
}
