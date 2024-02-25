package ast;

import co2.Symbol;
import java.util.List;
import types.Type;

public class FunctionDeclaration extends Node implements Declaration{

    private Type type;
    private Symbol identSymbol;
    private List<Symbol> params;
    private FunctionBody funcBody;

    public FunctionDeclaration(int lineNum, int charPos, String type, String ident, List<Symbol> params, FunctionBody funcBody){
        super(lineNum, charPos);
        identSymbol = new Symbol(type, ident);
        this.params = params;
        this.funcBody = funcBody;
    }

    public String function() {
        String print = "[" + identSymbol.name() + ":" + identSymbol.type();
        for (Symbol param :params) {
            print += ", " + param.name() + ":" + param.type();
        }
        return print;
    }

    public Symbol funcSym(){
        return identSymbol;
    }

    public List<Symbol> params(){
        return params;
    }

    public FunctionBody funcBody(){
        return funcBody;
    }
}
