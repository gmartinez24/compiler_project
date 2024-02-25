package ast;

public class RepeatStatement extends Node implements Statement{
    private StatementSequence repeatBlock;
    private Expression relation;
    public RepeatStatement(int lineNum, int charPos, StatementSequence repeatBlock, Expression relation) {
        super(lineNum, charPos);
        this.repeatBlock = repeatBlock;
        this.relation = relation;
    }

    public StatementSequence repeatBlock() {
        return repeatBlock;
    }

    public Expression relation() {
        return relation;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
