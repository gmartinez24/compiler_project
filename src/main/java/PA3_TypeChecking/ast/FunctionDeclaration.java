package ast;

import co2.Symbol;

public class FunctionDeclaration extends Node implements Declaration{

    private String type;

    private String ident;

    public FunctionDeclaration(int lineNum, int charPos, String type, String ident, FunctionBody funcBody){
        super(lineNum, charPos);
        this.type = type;
        this.ident = ident;
    }

    public Symbol symbol() {
        return new Symbol(type, ident);
    }
}
