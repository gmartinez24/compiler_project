package ast;

public interface Visitable {

    public void accept (NodeVisitor visitor){
        // From slides:
        // visitor.visitVariableDeclaration(this)
        visitor.visit();
    }
}
