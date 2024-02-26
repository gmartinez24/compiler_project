package ast;

//import java.beans.Expression;

public class Assignment extends Node implements Statement {
    Expression leftSide;
    Expression rightSide;

    String operator;

    public Assignment(int lineNum, int charPos, Expression leftSide, String operator, Expression rightSide) {
        super(lineNum, charPos);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public Expression lhs() {
        return this.leftSide;
    }

    public Expression rhs() {
        return this.rightSide;
    }

    public String operator() {return this.operator;}

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}

