package ast;

import co2.Symbol;
import java.util.List;

public class FunctionDeclaration extends Node implements Declaration{

    private String type;

    private String ident;

    private List<Symbol> params;

    public FunctionDeclaration(int lineNum, int charPos, String type, String ident, List<Symbol> params, FunctionBody funcBody){
        super(lineNum, charPos);
        this.type = type;
        this.ident = ident;
        this.params = params;
    }

    public String function() {
        Symbol funcSym = new Symbol(type, ident);
        String print = "[" + ident + ":" + type;
        for (Symbol param :params) {
            print += ", " + param.name() + ":" + param.type();
        }
        return print;
    }
}
