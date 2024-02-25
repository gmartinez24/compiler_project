package ast;

public class Multiplication extends Node implements Expression{
    Expression rhs;
    Expression lhs;
    public Multiplication(int lineNum, int charPos, Expression lhs, Expression rhs) {
        super(lineNum, charPos);
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
