package ast;

import types.FloatType;
import types.Type;

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

    @Override
    public Expression lhs() {
        return null;
    }

    @Override
    public Expression rhs() {
        return null;
    }

    @Override
    public Type type() {
        return new FloatType();
    }
}
