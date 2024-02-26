package ast;

import co2.Symbol;
import java.util.List;
import types.Type;

public class FunctionDeclaration extends Node implements Declaration{

    private Type type;
    private Symbol identSymbol;
    private List<Symbol> params;
    private FunctionBody body;


    public FunctionDeclaration(int lineNum, int charPos, Type type, String ident, List<Symbol> params, FunctionBody funcBody){
        super(lineNum, charPos);
        identSymbol = new Symbol(type, ident, lineNum, charPos);
        this.params = params;
        this.body = funcBody;
        this.type = type;
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

    public FunctionBody body(){
        return body;
    }

    public Type getType(){
        return type;
    }
    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
