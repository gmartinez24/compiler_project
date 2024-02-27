package ast;

import co2.Token;
import types.Type;

public class Relation extends Node implements Expression{

    //Relation takes in a relExpr ARE ALL RELEXPR - > EXPRESSIONS
    // ????
    // expression is an interface, what do we need to have abstracted in expression for all relExprs?

    //Or is it just a node, with a left and right expression
    private co2.Token op;
    private Expression lhs;
    private Expression rhs;
    private boolean empty;

    public Relation(int lineNum, int charPos, Expression lhs, Expression rhs, co2.Token op) {
        super(lineNum, charPos);
        empty = rhs == null && lhs == null;
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public String operator() {
        return op.lexeme();
    }

    @Override
    public Type type() {
        return lhs.type();
    }

    public Expression lhs() {
        return this.lhs;
    }

    public Expression rhs() {
        return this.rhs;
    }

    public boolean isEmpty () {return empty;}
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
