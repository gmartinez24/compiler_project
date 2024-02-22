package ast;


import java.util.List;

public class DeclerationList extends Node{

    // declaration list only holds a list of variable declarations
    private ArrayList<VariableDeclaration> vars;

    protected DeclerationList(int lineNum, int charPos) {
        super(lineNum, charPos);
    }

    // adds a new variable declaration to the list
    public addDeclaration(VariableDeclaration var) {
        vars.add(var);
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }
}
