package ast;


public class BoolLiteral extends Node implements Expression{
    private boolean value;
    private types.Type type ;

    public BoolLiteral(int lineNum, int charPos, String value) {
        super(lineNum, charPos);
        this.value = Boolean.parseBoolean(value);
        type = new types.BoolType();
    }

    public boolean value() {
        return this.value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
