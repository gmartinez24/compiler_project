package ast;

import java.util.ArrayList;
import java.util.List;

public class ArgumentList extends Node {
    int lineNum;
    int charPos;
    List<Expression> args;

    public ArgumentList(int lineNum, int charPos, List<Expression> args) {
        super(lineNum, charPos);
        this.args = args;
    }
}
