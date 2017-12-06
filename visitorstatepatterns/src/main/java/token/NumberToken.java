package token;

import visitors.TokenVisitor;

public class NumberToken implements Token {

    private final int value;

    public NumberToken(int value) {
        this.value = value;
    }


    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return "NUMBER (" + value + ')';
    }


    @Override
    public TokenType getType() {
        return TokenType.NUM;
    }


    public int getValue() {
        return value;
    }
}
