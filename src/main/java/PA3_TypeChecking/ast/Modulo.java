package ast;

public class Modulo extends Node implements Expression{
    Expression lhs;
    Expression rhs;

    public Modulo(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
    }
}
