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
    
    private StringBuilder errorBuffer;
    private Symbol currentFunction;

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

    }

    @Override
    public void visit(IntegerLiteral node) {

    }

    @Override
    public void visit(FloatLiteral node) {

    }

    @Override
    public void visit(Identifier node) {

    }

    @Override
    public void visit(LogicalNot node) {

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

    }

    @Override
    public void visit(StatementSequence node) {

    }

    @Override
    public void visit(VariableDeclaration node) {

    }

    @Override
    public void visit(FunctionBody node) {

    }

    @Override
    public void visit(FunctionDeclaration node) {

    }

    @Override
    public void visit(DeclarationList node) {

    }

    @Override
    public void visit (Computation node) {
        throw new RuntimeException("implement visit (Computation)");
    }

    public boolean check(AST ast){
        // TODO -> Write function
        return true;
    }
}
