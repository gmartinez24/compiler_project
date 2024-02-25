package ast;


// example of statement class
public class WhileStatement extends Node implements Statement{
    private Expression relation;
    private StatementSequence statSeq;



    public WhileStatement(int lineNum, int charPos, Expression relation, StatementSequence statSeq) {
        super(lineNum, charPos);
        this.relation = relation;
        this.statSeq = statSeq;

    }

    public Expression relation(){
        return this.relation;
    }

    public StatementSequence statSeq(){
        return this.statSeq;
    }
}
