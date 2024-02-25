package ast;

public class ReturnStatement extends Node implements Statement{

    Expression relation;

    public ReturnStatement(int lineNum, int charPos, Expression relation) {
        super(lineNum, charPos);
        this.relation = relation;
    }

    public Expression relation() {
        return relation;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
