package ast;

public interface Expression extends Visitable {
    public int lineNumber();
    public int charPosition();

    public Expression lhs();

    public Expression rhs();

}
