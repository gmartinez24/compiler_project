package ast;

public class FunctionBody extends Node{
    private DeclarationList vars;
    private StatementSequence statSeq;
    public FunctionBody(int lineNum, int charPos, DeclarationList dec, StatementSequence statSeq) {
        super(lineNum, charPos);
        vars = dec;
        this.statSeq = statSeq;
    }

    public DeclarationList vars(){
        return this.vars;
    }

    public StatementSequence funcSeq(){
        return statSeq;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
