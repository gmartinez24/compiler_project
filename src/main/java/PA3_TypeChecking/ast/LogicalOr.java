package ast;

public class LogicalOr extends Node implements Expression{
    Expression rhs;
    Expression lhs;
    public LogicalOr(int lineNum, int charPos, Expression lhs, Expression rhs) {
        super(lineNum, charPos);
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
