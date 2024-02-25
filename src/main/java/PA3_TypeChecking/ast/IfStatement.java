package ast;


public class IfStatement extends Node implements Statement{

    private Expression relation;
    private StatementSequence ifSeq;
    private StatementSequence elseSeq;

    public IfStatement(int lineNum, int charPos, Expression relation, StatementSequence ifStatSeq, StatementSequence elseStatSeq) {
        super(lineNum, charPos);
        ifSeq = ifStatSeq;
        elseSeq = elseStatSeq;
        this.relation = relation;
    }

    public Expression relation(){
        return relation;
    }

    public StatementSequence ifBlock(){
        return ifSeq;
    }

    public StatementSequence elseBlock(){
        return elseSeq;
    }
}

