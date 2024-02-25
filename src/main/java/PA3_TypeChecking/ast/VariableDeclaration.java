package ast;

import co2.Symbol;

public class VariableDeclaration extends Node implements Declaration{

    private Symbol sym;


    public VariableDeclaration(int lineNum, int charPos, String type, String ident){
        super(lineNum, charPos);
        sym = new Symbol(type, ident);
    }

    public Symbol symbol() {
        return this.sym;

    }
}
