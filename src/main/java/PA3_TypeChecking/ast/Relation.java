package ast;

public class Relation extends Node implements Expression{

    //Relation takes in a relExpr ARE ALL RELEXPR - > EXPRESSIONS
    // ????
    // expression is an interface, what do we need to have abstracted in expression for all relExprs?

    //Or is it just a node, with a left and right expression
    private co2.Token op;
    private Expression lhs;
    private Expression rhs;

    protected Relation(int lineNum, int charPos) {
        super(lineNum, charPos);
    }
}
