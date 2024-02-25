package ast;

//import java.beans.Expression;

public class Assignment extends Node implements Statement {
    Expression leftSide;
    Expression rightSide;

    public Assignment(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
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

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}

