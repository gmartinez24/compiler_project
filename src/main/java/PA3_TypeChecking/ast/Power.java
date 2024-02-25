package ast;

public class Power extends Node implements Expression{
    Expression lhs;
    Expression rhs;

    public Power(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
    }
}
