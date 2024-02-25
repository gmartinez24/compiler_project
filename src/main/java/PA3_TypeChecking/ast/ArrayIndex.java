package ast;

public class ArrayIndex extends Node implements Expression{
    protected ArrayIndex(int lineNum, int charPos) {
        super(lineNum, charPos);
    }
}
