package token;

import visitors.TokenVisitor;

public interface Token {

    void accept(TokenVisitor visitor);

    TokenType getType();
}