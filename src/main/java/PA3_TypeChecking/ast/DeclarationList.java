package ast;


import java.util.ArrayList;
import java.util.List;

public class DeclarationList extends Node{

    // declaration list only holds a list of variable declarations
    private List<Declaration> decs = new ArrayList<>();

    public DeclarationList(int lineNum, int charPos) {
        super(lineNum, charPos);
    }

    // adds a new variable declaration to the list
    public void addDeclaration(Declaration dec) {
        decs.add(dec);
    }

    public boolean empty() {
        return decs.isEmpty();
    }

    public List<Declaration> declarationList(){
        return this.decs;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
