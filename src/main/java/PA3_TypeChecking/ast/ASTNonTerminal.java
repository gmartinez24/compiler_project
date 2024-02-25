package ast;


public enum ASTNonTerminal {

    // Enum to standardize AST output representation.
    // Comments include a few examples of the expected output for each node, enums without any comment can be printed as is.

    Computation("Computation"), // Computation[main:()->void]
    //Done
    StatementSequence("StatementSequence"),

    Assignment("Assignment"),
    IfStatement("IfStatement"),
    WhileStatement("WhileStatement"),
    RepeatStatement("RepeatStatement"),
    ReturnStatement("ReturnStatement"),

    DeclarationList("DeclarationList"),
    //Done
    VariableDeclaration("VariableDeclaration"), // VariableDeclaration[input:int]

    FunctionDeclaration("FunctionDeclaration"), // FunctionDeclaration[power:(int,int)->int]
    //Done
    FunctionBody("FunctionBody"),
    //Done
    FunctionCall("FunctionCall"), // FunctionCall[power:(int,int)->int]
    ArgumentList("ArgumentList"),

    ArrayIndex("ArrayIndex"),

    Addition("Addition"),
    Subtraction("Subtraction"),
    Multiplication("Multiplication"),
    Division("Division"),
    Power("Power"),
    Modulo("Modulo"),

    LogicalAnd("LogicalAnd"),
    LogicalNot("LogicalNot"),
    LogicalOr("LogicalOr"),

    Relation("Relation"), // Relation[==]

    Identifier("Identifier"),
    FloatLiteral("FloatLiteral"), // FloatLiteral[1.0]
    IntegerLiteral("IntegerLiteral"), // IntegerLiteral[1]
    BoolLiteral("BoolLiteral"), // BoolLiteral[true]

    ;

    private String nodeName;
    ASTNonTerminal(String nodeName) {
        this.nodeName = nodeName;
    }


    @Override
    public String toString(){
        return nodeName;
    }

}



