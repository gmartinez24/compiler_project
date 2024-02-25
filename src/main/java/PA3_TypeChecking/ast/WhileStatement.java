package ast;


// example of statement class
public class WhileStatement extends Node {
    private Relation relation;
    private StatementSequence statSeq;



    protected WhileStatement(int lineNum, int charPos, Relation relation, StatementSequence statSeq) {
        super(lineNum, charPos);
        this.relation = relation;
        this.statSeq = statSeq;

    }

    public Relation relation(){
        return this.relation;
    }

    public StatementSequence statSeq(){
        return this.statSeq;
    }
}
