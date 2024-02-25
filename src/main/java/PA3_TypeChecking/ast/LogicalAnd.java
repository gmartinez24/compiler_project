package ast;

public class LogicalAnd extends Node implements Expression {
    Expression lhs;
    Expression rhs;

    public LogicalAnd(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
    }
}
