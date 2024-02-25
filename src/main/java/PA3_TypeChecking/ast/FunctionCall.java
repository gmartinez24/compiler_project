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
}
