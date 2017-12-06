package token;

import visitors.TokenVisitor;

public class Brace implements Token {

    private final TokenType type;


    public Brace(TokenType type) {
        this.type = type;
    }


    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public TokenType getType() {
        return type;
    }


    @Override
    public String toString() {
        if (type.equals(TokenType.LPAREN)) {
            return "(";
        }
        else {
            return ")";
        }
    }
}
