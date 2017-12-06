package visitors;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private Stack<Integer> ans = new Stack<>();


    public int calculate(Stack<Token> tokens) {
        tokens.forEach(token -> token.accept(this));
        if (ans.size() != 1) {
            throw new IllegalStateException("Something went really bad: tokens " + tokens + ", result stack " + ans);
        }

        return ans.pop();
    }


    @Override
    public void visit(NumberToken token) {
        ans.push(token.getValue());
    }


    @Override
    public void visit(Brace token) {
        throw new IllegalStateException("Something went really wrong: at the calculation stage there should not be any brackets");
    }


    @Override
    public void visit(Operation token) {
        if (ans.size() < 2) {
            throw new IllegalStateException("Expression is not valid");
        }

        int second = ans.pop();
        int first = ans.pop();

        switch (token.getType()) {
            case DIV:
                ans.push(first / second);
                break;
            case MUL:
                ans.push(first * second);
                break;
            case PLUS:
                ans.push(first + second);
                break;
            case MINUS:
                ans.push(first - second);
                break;
        }
    }
}
