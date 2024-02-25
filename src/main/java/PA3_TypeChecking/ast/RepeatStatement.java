package ast;

public class RepeatStatement extends Node implements Statement{
    StatementSequence repeatBlock;
    Expression relation;
    public RepeatStatement(int lineNum, int charPos, StatementSequence repeatBlock, Expression relation) {
        super(lineNum, charPos);
        this.repeatBlock = repeatBlock;
        this.relation = relation;
    }
}
