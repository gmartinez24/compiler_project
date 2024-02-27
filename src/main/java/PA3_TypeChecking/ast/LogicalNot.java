package ast;

import types.BoolType;
import types.Type;

public class LogicalNot extends Node implements Expression {
    private Expression negated;
    public LogicalNot(int lineNum, int charPos, Expression negated) {
        super(lineNum, charPos);
        this.negated = negated;
    }

    public Expression expression() {
        return this.negated;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Type type() {
        return new BoolType();
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
