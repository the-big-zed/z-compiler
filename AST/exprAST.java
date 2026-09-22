package AST;


// this probably needs a type field, but we'll figure that out when we'll need a type checker
public class exprAST {
    exprAST() {

    }
}

class NumberExprAST extends exprAST {
    private final double val;

    public NumberExprAST(final double val) {
        this.val = val;
    }
}

class VariableExprAST extends exprAST {
    private final String name;

    public VariableExprAST(final String name) {
        this.name = name;
    }
}

class BinaryExprAST extends exprAST {
    private final char op;
    private final exprAST left;
    private final exprAST right;


    public BinaryExprAST(final char op, final exprAST left, final exprAST right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }
}

class CallExprAST extends exprAST {
    private final String Callee;
    private final exprAST[] args;

    public CallExprAST(final String Callee, final exprAST[] args) {
        this.Callee = Callee;
        this.args = args;
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



