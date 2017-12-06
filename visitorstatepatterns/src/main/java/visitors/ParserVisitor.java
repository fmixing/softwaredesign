package visitors;

import token.*;

import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private final Stack<Token> rpnStack = new Stack<>();

    private final Stack<Token> parsingStack = new Stack<>();

    private Token prevToken;


    public Stack<Token> parse(List<Token> tokens) {
        tokens.forEach(token -> token.accept(this));

        while (!parsingStack.empty() && parsingStack.peek() instanceof Operation) {
            rpnStack.push(parsingStack.pop());
        }

        if (!parsingStack.empty()) {
            throw new IllegalStateException("Parsing stack contains brackets at the end of the expression: " + parsingStack);
        }

        return rpnStack;
    }


    @Override
    public void visit(NumberToken token) {
        if (prevToken != null && prevToken instanceof NumberToken) {
            throw new IllegalStateException("You can't have multiple integers in the expression in a row");
        }
        prevToken = token;

        rpnStack.push(token);
    }


    @Override
    public void visit(Brace token) {
        prevToken = token;

        if (token.getType().equals(TokenType.LPAREN))
        {
            parsingStack.push(token);
            return;
        }

        while (!parsingStack.empty() && !parsingStack.peek().getType().equals(TokenType.LPAREN)) {
            rpnStack.push(parsingStack.pop());
        }

        if (parsingStack.empty()) {
            throw new IllegalStateException("Parsing stack should contain left bracket, but it's missing");
        }

        parsingStack.pop();
    }


    private int priority(Token token) {
        if (token.getType().equals(TokenType.DIV) || token.getType().equals(TokenType.MUL)) {
            return 1;
        }
        if (token.getType().equals(TokenType.PLUS) || token.getType().equals(TokenType.MINUS)) {
            return 0;
        }
        return -1;
    }


    @Override
    public void visit(Operation token) {
        if (prevToken != null && prevToken instanceof Operation) {
            throw new IllegalStateException("You can't have multiple operations in the expression in a row");
        }
        prevToken = token;

        if (parsingStack.empty()) {
            parsingStack.push(token);
            return;
        }

        while (!parsingStack.isEmpty() && priority(token) <= priority(parsingStack.peek())) {
            rpnStack.push(parsingStack.pop());
        }

        parsingStack.push(token);
    }
}
