package ast;

import types.Type;

import java.security.spec.ECPoint;

public class Power extends Node implements Expression{
    Expression lhs;
    Expression rhs;

    public Power(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
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
