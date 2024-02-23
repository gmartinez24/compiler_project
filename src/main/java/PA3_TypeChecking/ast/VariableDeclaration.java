package ast;

import co2.Symbol;

public class VariableDeclaration extends Node implements Declaration{

    private String type;

    private String ident;

    public VariableDeclaration(int lineNum, int charPos, String type, String ident){
        super(lineNum, charPos);
        this.type = type;
        this.ident = ident;
    }

    public Symbol symbol() {
        return new Symbol(type, ident);
    }
}
