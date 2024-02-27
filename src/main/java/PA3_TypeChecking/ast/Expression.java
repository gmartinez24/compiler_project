package ast;

public interface Expression extends Visitable {
    public types.Type type();
    public int lineNumber();
    public int charPosition();

    public Expression lhs();

    public Expression rhs();

}
