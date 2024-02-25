package ast;

public class LogicalNot extends Node implements Expression {
    private Expression negated;
    public LogicalNot(int lineNum, int charPos, Expression negated) {
        super(lineNum, charPos);
        this.negated = negated;
    }
}
