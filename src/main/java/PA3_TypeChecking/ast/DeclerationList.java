package ast;

public class DeclerationList extends Node{

    protected DeclerationList(int lineNum, int charPos) {
        super(lineNum, charPos);
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }
}
