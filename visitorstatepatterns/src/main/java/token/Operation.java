package token;

import visitors.TokenVisitor;

public class Operation implements Token {

    private final TokenType type;


    public Operation(TokenType type) {
        this.type = type;
    }


    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public String toString() {
        return type.name();
    }


    @Override
    public TokenType getType() {
        return type;
    }
}
