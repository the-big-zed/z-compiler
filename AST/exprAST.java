package AST;


// this probably needs a type field, but we'll figure that out when we'll need a type checker
public class exprAST {
    exprAST() {

    }
}

public exprAST LogError(final String err) {
    System.out.printf("Error: %s", err);
    return null;
}

public PrototypeAST LogErrorP(final String err) {
    System.out.printf("Error: %s",err);
    return null;
}

class NumberExprAST extends exprAST {
    private final double val;

    public NumberExprAST(final double Val) {
        val = Val;
    }
}

class VariableExprAST extends exprAST {
    private final String name;

    public VariableExprAST(final String Name) {
        name = Name;
    }
}

class BinaryExprAST extends exprAST {
    private final char op;
    private final exprAST left;
    private final exprAST right;


    public BinaryExprAST(final char Op, final exprAST Left, final exprAST Right) {
        op = Op;
        left = Left;
        right = Right;
    }
}

class CallExprAST extends exprAST {
    private final String Callee;
    private final exprAST[] args;

    public CallExprAST(final String callee, final exprAST[] Args) {
        Callee = callee;
        args = Args;
    }
}

class PrototypeAST {
    private final String name;
    private final String[] args;

    public PrototypeAST(final String name, final String[] args) {
        this.name = name;
        this.args = args;
    }

    public final String getName() { return name; }
}

class FunctionAST {
    private final PrototypeAST Proto;
    private final exprAST body;

    public FunctionAST(final PrototypeAST Proto, final exprAST body) {
        this.Proto = Proto;
        this.body = body;
    }
}



