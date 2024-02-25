package ast;

import co2.Symbol;

public class VariableDeclaration extends Node implements Declaration{

    private Symbol sym;


    public VariableDeclaration(int lineNum, int charPos, types.Type type, String ident){
        super(lineNum, charPos);
        sym = new Symbol(type, ident, lineNum, charPos);
    }

    public Symbol symbol() {
        return this.sym;

    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
