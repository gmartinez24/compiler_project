package ast;

public class Subtraction extends Node implements Expression{
    Expression lhs;
    Expression rhs;

    public Subtraction(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
    }
}
