package ast;

public class Division extends Node implements Expression {
    Expression lhs;
    Expression rhs;

    public Division(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.lhs = leftSide;
        this.rhs = rightSide;
    }


}
