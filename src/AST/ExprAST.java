package src.AST;


import java.util.List;

// this probably needs a type field, but we'll figure that out when we'll need a type checker
public class ExprAST {
    ExprAST() {

    }

    public static ExprAST LogError(final String err) {
        System.out.printf("Error: %s", err);
        return null;
    }

    public static PrototypeAST LogErrorP(final String err) {
        System.out.printf("Error: %s",err);
        return null;
    }

    public static class NumberExprAST extends ExprAST {
        private final double val;

        public NumberExprAST(final double Val) {
            val = Val;
        }
    }

    public static class VariableExprAST extends ExprAST {
        private final String name;

        public VariableExprAST(final String Name) {
            name = Name;
        }
    }

    public static class BinaryExprAST extends ExprAST {
        private final char op;
        private final ExprAST left;
        private final ExprAST right;


        public BinaryExprAST(final char Op, final ExprAST Left, final ExprAST Right) {
            op = Op;
            left = Left;
            right = Right;
        }
    }

    public static class CallExprAST extends ExprAST {
        private final String Callee;
        private final List<ExprAST> args;

        public CallExprAST(final String callee, final List<ExprAST> Args) {
            Callee = callee;
            args = Args;
        }
    }

    public static class PrototypeAST {
        private final String name;
        private final List<String> args;

        public PrototypeAST(final String name, final List<String> args) {
            this.name = name;
            this.args = args;
        }

        public final String getName() { return name; }
    }

    public static class FunctionAST {
        private final PrototypeAST Proto;
        private final ExprAST body;

        public FunctionAST(final PrototypeAST Proto, final ExprAST body) {
            this.Proto = Proto;
            this.body = body;
        }
    }
}





