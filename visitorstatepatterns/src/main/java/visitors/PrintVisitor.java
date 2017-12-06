package visitors;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.Stack;

public class PrintVisitor implements TokenVisitor {

    private StringBuilder stringBuilder = new StringBuilder();


    public void print(Stack<Token> tokens) {
        tokens.forEach(token -> token.accept(this));
        System.out.println(stringBuilder.toString());
    }


    @Override
    public void visit(NumberToken token) {
        stringBuilder.append(token.toString()).append(" ");
    }


    @Override
    public void visit(Brace token) {
        throw new IllegalStateException("Something went really wrong: at the printing stage there should not be any brackets");
    }


    @Override
    public void visit(Operation token) {
        stringBuilder.append(token.toString()).append(" ");
    }
}
