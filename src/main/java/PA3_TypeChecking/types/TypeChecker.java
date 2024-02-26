package types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ast.*;
import co2.Symbol;

public class TypeChecker implements NodeVisitor {


    //The TypeChecker class is another implementation of the node visitor
    //Based on the visitor pattern. Starting at the root node (computation)
    //We will traverse the tree and commit type checking via the specific type classes
    //In the types directory.
    
    private StringBuilder errorBuffer = new StringBuilder();
    private Symbol currentFunction;
    private Type currType = new VoidType();


    /* 
     * Useful error strings:
     *
     * "Call with args " + argTypes + " matches no function signature."
     * "Call with args " + argTypes + " matches multiple function signatures."
     * 
     * "IfStat requires relation condition not " + cond.getClass() + "."
     * "WhileStat requires relation condition not " + cond.getClass() + "."
     * "RepeatStat requires relation condition not " + cond.getClass() + "."
     * 
     * "Function " + currentFunction.name() + " returns " + statRetType + " instead of " + funcRetType + "."
     * 
     * "Variable " + var.name() + " has invalid type " + var.type() + "."
     * "Array " + var.name() + " has invalid base type " + baseType + "."
     * 
     * 
     * "Function " + currentFunction.name() + " has a void arg at pos " + i + "."
     * "Function " + currentFunction.name() + " has an error in arg at pos " + i + ": " + ((ErrorType) t).message())
     * "Not all paths in function " + currentFunction.name() + " return."
     */

    

    private void reportError (int lineNum, int charPos, String message) {
        errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
        errorBuffer.append("[" + message + "]" + "\n");
    }

    public boolean hasError () {
        return errorBuffer.length() != 0;
    }


    public String errorReport () {
        return errorBuffer.toString();
    }

    @Override
    public void visit(BoolLiteral node) {
        currType = new BoolType();
    }

    @Override
    public void visit(IntegerLiteral node) {
        currType = new IntType();
    }

    @Override
    public void visit(FloatLiteral node) {
        currType = new FloatType();
    }

    @Override
    public void visit(Identifier node) {
        currType = node.symbol().type();
    }

    @Override
    public void visit(LogicalNot node) {
        node.expression().accept(this);
        Type negateType = currType;
        negateType.not();

    }

    @Override
    public void visit(Power node) {

    }

    @Override
    public void visit(Multiplication node) {

    }

    @Override
    public void visit(Division node) {

    }

    @Override
    public void visit(Modulo node) {

    }

    @Override
    public void visit(LogicalAnd node) {

    }

    @Override
    public void visit(Addition node) {
        //Types.
    }

    @Override
    public void visit(Subtraction node) {

    }

    @Override
    public void visit(LogicalOr node) {

    }

    @Override
    public void visit(Relation node) {

    }

    @Override
    public void visit(Assignment node) {

    }

    @Override
    public void visit(ArgumentList node) {

    }

    @Override
    public void visit(FunctionCall node) {

    }

    @Override
    public void visit(IfStatement node) {

    }

    @Override
    public void visit(WhileStatement node) {

    }

    @Override
    public void visit(RepeatStatement node) {

    }

    @Override
    public void visit(ReturnStatement node) {
        //We need to get the return statements type and match to the func return type
        //Need to process relation, make sure it's type == return type of function statement
        if(currentFunction.type().getClass() == )
    }

    @Override
    public void visit(StatementSequence node) {
        for(Statement s: node.getStatements()){
            s.accept(this);
        }
    }

    @Override
    public void visit(VariableDeclaration node) {

    }

    @Override
    public void visit(FunctionBody node) {
        //Don't need to typecheck variable decs
        System.out.println(currentFunction.toString());
        node.funcSeq().accept(this);
    }

    @Override
    public void visit(FunctionDeclaration node) {
        //Not checking functions at the moment, just going to statSeq
        currentFunction = node.funcSym();
        node.body().accept(this);
    }

    @Override
    public void visit(DeclarationList node) {
        if(node.empty()) return;
        //Visit all the function declarations
        for(Declaration d: node.declarationList()){
            if(d instanceof FunctionDeclaration){
                d.accept(this);
            }
        }
    }

    @Override
    public void visit (Computation node) {
        //Don't need to error check vars declarations list, but will need to visit functions
        if(node.functions() != null){
            node.functions().accept(this);
        }
        node.mainStatementSequence().accept(this);
    }

    public boolean check(AST ast){
        ast.getRoot().accept(this);
        return hasError();

    }
}
