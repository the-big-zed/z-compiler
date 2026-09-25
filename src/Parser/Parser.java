package Parser;

import lexer.Lexer;
import AST.*;
import java.io.IOException;

// needs to be fixed
public class Parser {
    private static int curTok;
    private final Lexer lex;

    public Parser(Lexer lex) {
        this.lex = lex;
    }

    private int Next() throws IOException {
        curTok = lex.GetTok();
        return curTok;
    }
}
