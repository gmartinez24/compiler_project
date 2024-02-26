package ast;

import co2.Symbol;

import java.util.List;

public class Identifier extends Node implements Expression{
    private List<Expression> indexList;
    private Symbol identSymbol;

    public Identifier(int lineNum, int charPos, Symbol sym, List<Expression> indexes) {
        super(lineNum, charPos);
        this.identSymbol = sym;
        if (!indexes.isEmpty()) {
            indexList = indexes;
        }else {
            indexList = null;
        }
    }

    public Symbol symbol() {
        return identSymbol;
    }

    public List<Expression> indexList() {return indexList;}

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Expression lhs() {
        return null;
    }

    @Override
    public Expression rhs() {
        return null;
    }
}
