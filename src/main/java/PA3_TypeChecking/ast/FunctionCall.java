package ast;
import java.util.List;
public class FunctionCall extends Node implements Statement,Expression{
    co2.Symbol funcSym;
    ArgumentList args;

    public FunctionCall(int lineNum, int charPos, co2.Symbol funcSym, ArgumentList args) {
        super(lineNum, charPos);
        this.args = args;
        this.funcSym = funcSym;
    }

    public co2.Symbol symbol() {
        return funcSym;
    }

    public ArgumentList args() {
        return args;
    }

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

    public types.Type type() {return funcSym.type();}
}
