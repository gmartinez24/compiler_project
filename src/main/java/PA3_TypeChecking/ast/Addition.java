package ast;

public class Addition extends Node implements Expression{
    Expression lhs;
    Expression rhs;
    public Addition(int lineNum, int charPos, Expression lhs, Expression rhs) {
        super(lineNum, charPos);
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
