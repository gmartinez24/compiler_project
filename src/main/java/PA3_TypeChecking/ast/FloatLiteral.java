package ast;

import static java.lang.Float.parseFloat;

public class FloatLiteral extends Node implements Expression{
    private float value;
    private types.Type type ;

    public FloatLiteral(int lineNum, int charPos, String value) {
        super(lineNum, charPos);
        this.value = parseFloat(value);
        type = new types.FloatType();
    }

    public float value() {
        return this.value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
