import token.Token;
import visitors.ParserVisitor;
import visitors.CalcVisitor;
import visitors.PrintVisitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String expr = reader.readLine();

            List<Token> tokens = new Tokenizer().getTokens(expr);
            Stack<Token> rpn = new ParserVisitor().parse(tokens);

            new PrintVisitor().print(rpn);
            int calculate = new CalcVisitor().calculate(rpn);
            System.out.println(calculate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}
