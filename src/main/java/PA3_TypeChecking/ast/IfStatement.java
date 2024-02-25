package ast;


public class IfStatement extends Node{

    private Relation relation;
    private StatementSequence ifSeq;
    private StatementSequence elseSeq;

    protected IfStatement(int lineNum, int charPos, StatementSequence ifStatSeq, StatementSequence elseStatSeq) {
        super(lineNum, charPos);
        ifSeq = ifStatSeq;
        elseSeq = elseStatSeq;
    }

    public Relation relation(){
        return relation;
    }

    public StatementSequence ifBlock(){
        return ifSeq;
    }

    public StatementSequence elseBlock(){
        return elseSeq;
    }
}

