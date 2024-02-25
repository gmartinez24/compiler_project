package ast;

import static java.lang.Integer.parseInt;

public class IntegerLiteral extends Node implements Expression{

    private int value;
    private types.Type type ;

    public IntegerLiteral(int lineNum, int charPos, String value) {
        super(lineNum, charPos);
        this.value = parseInt(value);
        type = new types.IntType();
    }

    public int value() {
        return this.value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
