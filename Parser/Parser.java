package Parser;

import lexer.Lexer;
import AST.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// needs to be fixed
public class Parser {
    private static int curTok;
    private static Lexer lex = null;
    public static final HashMap<Character, Integer> BinOpPrecedence = new HashMap<>();
    static { // TODO check this
        BinOpPrecedence.put('<', 10);
        BinOpPrecedence.put('*', 40);
        BinOpPrecedence.put('-', 20);
        BinOpPrecedence.put('+', 20);
        BinOpPrecedence.put('=', 20);
        BinOpPrecedence.put('>', 10);
    }


    public Parser(Lexer lex) {
        Parser.lex = lex;
    }


    public static void MainLoop() throws IOException {
        while (true) {
            Lexer.Tokens token = Lexer.Tokens.fromValues(curTok);

            if (token == Lexer.Tokens.EOF) {
                return;
            }
            else if (curTok == ';') {
                Next();
            }
            else if (token == Lexer.Tokens.FUNC) {
                HandleDefinition();
            }
            else {
                ExprAST.LogError("Unknown error (default case)");
            }

        }
    }

    public static void HandleDefinition() throws IOException {
        ExprAST.FunctionAST e = ParseDefinition();
        if (e != null ) {
            System.out.println("Parsed a function");
        }
        else {
            Next();
        }
    }

    private static int Next() throws IOException {
        curTok = lex.GetTok();
        return curTok;
    }

    public static ExprAST ParseNumberExpr(double numVal) throws IOException {
        ExprAST result = new ExprAST.NumberExprAST(numVal);

        Next();

        return result;
    }

    public static ExprAST ParseParenExpr() throws IOException {
        Next();

        ExprAST V = ParseExpression();

        if ( V == null ){
            return null;
        }

        if (curTok != ')') {
            return ExprAST.LogError("expected ')'");
        }
        Next();
        return V;
    }

    public static ExprAST ParseIdentifierExpr() throws IOException {
        String IdName = lex.IdentifierStr;

        Next();

        if (curTok != '(') {
            return new ExprAST.VariableExprAST(IdName);
        }

        Next();

        List<ExprAST> args = new ArrayList<>();
        if (curTok != ')') {
            while(true) {
                ExprAST arg = ParseExpression();
                if (arg != null) {
                    args.add(arg);
                }
                else {
                    return null;
                }

                if (curTok == ')') {
                    break;
                }

                if (curTok != '|') {
                    return ExprAST.LogError("Expected ')' or '|' in argument list");
                }

                Next();
            }
        }

        Next();

        return new ExprAST.CallExprAST(IdName, args);
    }

    public static ExprAST ParsePrimary() throws IOException {
        Lexer.Tokens token = Lexer.Tokens.fromValues(curTok);

        if (token == Lexer.Tokens.IDENTIFIER) {
            return ParseIdentifierExpr();
        }
        else if (token == Lexer.Tokens.NUMBER) {
            return ParseNumberExpr(lex.NumVal);
        }
        else if (curTok == '(') {
            return ParseParenExpr();
        }
        else {
            return ExprAST.LogError("unknown token when expecting an expression");
        }
    }

    public static int GetTokPrecedence() {
        if (!Character.isDefined(curTok)) { // TODO this should be isascii
            return -1;
        }

        int TokPrec = BinOpPrecedence.get(curTok);
        if (TokPrec <= 0) return -1;
        return TokPrec;
    }

    public static ExprAST ParseExpression() throws IOException {
        ExprAST LHS = ParsePrimary();
        if (LHS == null) {
            return null;
        }

        return ParseBinOpRHS(0, LHS);
    }

    public static ExprAST ParseBinOpRHS(int ExprPrec, ExprAST LHS) throws IOException {

        while (true) {
            int TokPrec = GetTokPrecedence();

            if (TokPrec < ExprPrec) {
                return LHS;
            }

            int BinOp = curTok;
            Next();

            ExprAST RHS = ParsePrimary();
            if (RHS == null) {
                return null;
            }

            int NextPrec = GetTokPrecedence();
            if (TokPrec < NextPrec) {
                RHS = ParseBinOpRHS(TokPrec+1, RHS);
                if (RHS == null) {
                    return null;
                }
            }

            ExprAST.BinaryExprAST LSH = new ExprAST.BinaryExprAST((char) BinOp, LHS, RHS);

        }

    }

    public static ExprAST.PrototypeAST ParsePrototype() throws IOException {
        if (curTok != Lexer.Tokens.IDENTIFIER.value) {
            return ExprAST.LogErrorP("Expected '(' in prototype");
        }

        String FnName = lex.IdentifierStr;
        Next();

        List<String> ArgNames = new ArrayList<>(List.of());
        while (Next() == Lexer.Tokens.IDENTIFIER.value) {
            ArgNames.add(lex.IdentifierStr);
        }

        if (curTok != ')') {
            return ExprAST.LogErrorP("Expected ')' in prototype");
        }

        Next();

        return new ExprAST.PrototypeAST(FnName, ArgNames);
    }

    public static ExprAST.FunctionAST ParseDefinition() throws IOException {
        Next();
        ExprAST.PrototypeAST Proto = ParsePrototype();
        if (Proto == null) {
            return null;
        }

        ExprAST E = ParseExpression();
        if (E != null) {
            return new ExprAST.FunctionAST(Proto, E);
        }

        return null;
    }

    // TODO add import parsing

}
