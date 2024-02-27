package ast;

import static java.lang.Integer.parseInt;

public class IntegerLiteral extends Node implements Expression{

    private int value;
    private types.Type type ;

    public IntegerLiteral(int lineNum, int charPos, String value) {
        super(lineNum, charPos);
        this.value = parseInt(value);
        type = new types.IntType();
    }

    public int value() {
        return this.value;
    }

    public types.Type type() {
        return type;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Expression lhs() {
        return null;
    }

    @Override
    public Expression rhs() {
        return null;
    }
}
