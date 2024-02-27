package ast;

import types.Type;

public class LogicalOr extends Node implements Expression{
    Expression rhs;
    Expression lhs;
    public LogicalOr(int lineNum, int charPos, Expression lhs, Expression rhs) {
        super(lineNum, charPos);
        this.lhs = lhs;
        this.rhs = rhs;
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

    @Override
    public Type type() {
        return lhs.type();
    }
}
