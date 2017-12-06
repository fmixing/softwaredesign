import token.*;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public List<Token> getTokens(String input) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            StringBuilder num = new StringBuilder();

            while (Character.isDigit(c)) {
                num.append(c);
                i++;
                if (i == input.length()) {
                    c = ' ';
                    break;
                }
                c = input.charAt(i);
            }

            if (!(Character.isWhitespace(c) || c == ')' || i < input.length())) {
                throw new IllegalStateException("There is some strange symbol following number: '" + c + "'");
            }

            if (!num.toString().equals("")) {
                tokens.add(new NumberToken(Integer.parseInt(num.toString())));
            }

            switch (c) {
                case '+':
                    tokens.add(new Operation(TokenType.PLUS));
                    break;
                case '-':
                    tokens.add(new Operation(TokenType.MINUS));
                    break;
                case '/':
                    tokens.add(new Operation(TokenType.DIV));
                    break;
                case '*':
                    tokens.add(new Operation(TokenType.MUL));
                    break;
                case '(':
                    tokens.add(new Brace(TokenType.LPAREN));
                    break;
                case ')':
                    tokens.add(new Brace(TokenType.RPAREN));
                    break;
                case ' ':
                    break;
                default:
                    throw new RuntimeException("Unknown type of token: '" + c + "'");
            }
        }

        return tokens;
    }

}
