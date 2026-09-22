package Parser;

import lexer.Lexer;

// needs to be fixed
public class Parser {
    private static int curTok;
    private final Lexer lex;



    private static int getNextToken() {
        return curTok = gettok();
    }
}
