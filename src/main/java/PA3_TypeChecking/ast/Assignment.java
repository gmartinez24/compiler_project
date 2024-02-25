package ast;

//import java.beans.Expression;

public class Assignment extends Node implements Statement{
    Expression leftSide;
    Expression rightSide;

    Expression unaryOp;
    public Assignment(int lineNum, int charPos, Expression leftSide, Expression rightSide) {
        super(lineNum, charPos);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }


}
