package ast;

import types.Type;

public class Addition extends Node implements Expression{
    Expression lhs;
    Expression rhs;
    public Addition(int lineNum, int charPos, Expression lhs, Expression rhs) {
        super(lineNum, charPos);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Type type() {
        return lhs().type();
    }

    public Expression lhs() {
        return this.lhs;
    }

    public Expression rhs() {
        return this.rhs;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
