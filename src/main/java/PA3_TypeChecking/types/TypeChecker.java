package types;

import java.util.*;

import ast.*;
import co2.Symbol;

public class TypeChecker implements NodeVisitor {


    //The TypeChecker class is another implementation of the node visitor
    //Based on the visitor pattern. Starting at the root node (computation)
    //We will traverse the tree and commit type checking via the specific type classes
    //In the types directory.
    
    private StringBuilder errorBuffer = new StringBuilder();
    private Symbol currentFunction;
    private ArrayList<FuncType> functionList;
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
        System.out.println("bool");
        currType = new BoolType();
    }

    @Override
    public void visit(IntegerLiteral node) {
        System.out.println("int");
        currType = new IntType();
    }

    @Override
    public void visit(FloatLiteral node) {
        System.out.println("float");
        currType = new FloatType();
    }

    @Override
    public void visit(Identifier node) {
        System.out.println("ident");
        currType = node.symbol().type();
    }

    @Override
    public void visit(LogicalNot node) {
        System.out.println("not");
        node.expression().accept(this);
        Type negateType = currType;
        if(negateType.not() instanceof ErrorType){
            System.out.println("not err");
            reportError(node.lineNumber(), node.charPosition(), negateType.not().toString());
        }

    }

    @Override
    public void visit(Power node) {
        System.out.println("pow");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.pow(rhs) instanceof ErrorType){
            System.out.println("pow err");
            reportError(node.lineNumber(), node.charPosition(), lhs.pow(rhs).toString());
        }
    }

    @Override
    public void visit(Multiplication node) {
        System.out.println("mult");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.mul(rhs) instanceof ErrorType){
            System.out.println("mult err");
            reportError(node.lineNumber(), node.charPosition(), lhs.mul(rhs).toString());
        }
    }

    @Override
    public void visit(Division node) {
        System.out.println("div");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.div(rhs) instanceof ErrorType){
            System.out.println("div err");
            reportError(node.lineNumber(), node.charPosition(), lhs.div(rhs).toString());
        }
    }

    @Override
    public void visit(Modulo node) {
        System.out.println("mod");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.mod(rhs) instanceof ErrorType){
            System.out.println("mod err");
            reportError(node.lineNumber(), node.charPosition(), lhs.mul(rhs).toString());
        }
    }

    @Override
    public void visit(LogicalAnd node) {
        System.out.println("and");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.and(rhs) instanceof ErrorType){
            System.out.println("and err");
            reportError(node.lineNumber(), node.charPosition(), lhs.and(rhs).toString());
        }
    }

    @Override
    public void visit(Addition node) {
        System.out.println("add");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.add(rhs) instanceof ErrorType){
            System.out.println("add err");
            reportError(node.lineNumber(), node.charPosition(), lhs.add(rhs).toString());
        }
    }

    @Override
    public void visit(Subtraction node) {
        System.out.println("sub");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.sub(rhs) instanceof ErrorType){
            System.out.println("sub err");
            reportError(node.lineNumber(), node.charPosition(), lhs.sub(rhs).toString());
        }
    }

    @Override
    public void visit(LogicalOr node) {
        System.out.println("or");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.mod(rhs) instanceof ErrorType){
            System.out.println("or err");
            reportError(node.lineNumber(), node.charPosition(), lhs.mul(rhs).toString());
        }
    }

    @Override
    public void visit(Relation node) {
        System.out.println("relation");
        System.out.println(node.toString());
        if(node.lhs() == null){
            System.out.println("rel err");
            reportError(node.lineNumber(), node.charPosition(), "Rel err");
            return;
        }

        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.getClass() != rhs.getClass()){
            System.out.println("rel err");
            reportError(node.lineNumber(), node.charPosition(), "Cannot compare " + lhs.getClass() + " " + rhs.getClass());
        }

    }

    @Override
    public void visit(Assignment node) {
        System.out.println("assign");
        node.lhs().accept(this);
        Type lhs = currType;
        node.rhs().accept(this);
        Type rhs = currType;
        if(lhs.getClass() != rhs.getClass()){
            System.out.println("assign err");
            reportError(node.lineNumber(), node.charPosition(), "Cannot compare " + lhs.getClass() + " " + rhs.getClass());
        }
    }

    @Override
    public void visit(ArgumentList node) {
        System.out.println("argList");
        //Do nothing for now -> Through
    }

    @Override
    public void visit(FunctionCall node) {
        System.out.println("funcCall");

        //Gathering types that are called
        TypeList inputTypes = new TypeList();
        for(Expression a: node.args().args()){
            a.accept(this);
            inputTypes.add(currType);
        }
        //At this point, we have an argument list

        //Need to find a matching function
        //node.symbol().name()
        //FIND NAME, MATCH ARGUMENT

    }

    @Override
    public void visit(IfStatement node) {
        System.out.println("if");
        node.relation().accept(this);
        if(!(currType instanceof BoolType)){
            System.out.println("if err");
            reportError(node.lineNumber(), node.charPosition(), "IfStat requires relation condition not" + currType.getClass());
        }
        node.ifBlock().accept(this);
        node.elseBlock().accept(this);

    }

    @Override
    public void visit(WhileStatement node) {
        System.out.println("while stmt");
        node.relation().accept(this);
        if(!(currType instanceof BoolType)){
            System.out.println("while err");
            reportError(node.lineNumber(), node.charPosition(), "WhileStat requires relation condition not" + currType.getClass());
        }
        node.statSeq().accept(this);
    }

    @Override
    public void visit(RepeatStatement node) {
        System.out.println("rep stmt");
        node.relation().accept(this);
        if(!(currType instanceof BoolType)){
            System.out.println("repeat err");
            reportError(node.lineNumber(), node.charPosition(), "WhileStat requires relation condition not" + currType.getClass());
        }
        node.repeatBlock().accept(this);
    }

    @Override
    public void visit(ReturnStatement node) {
        System.out.println("ret stmt");
        node.relation().accept(this);
        if(currentFunction.type() != currType){
            reportError(node.lineNumber(), node.charPosition(), "Function " + currentFunction.name() + " returns " + currType + " instead of " + currentFunction.type() + ".");
        }

    }

    @Override
    public void visit(StatementSequence node) {
        System.out.println("statseq");
        for(Statement s: node.getStatements()){
            s.accept(this);
        }
    }

    @Override
    public void visit(VariableDeclaration node) {

    }

    @Override
    public void visit(FunctionBody node) {
        System.out.println("funcbod");
        //Don't need to typecheck variable decs
        System.out.println(currentFunction.toString());
        node.funcSeq().accept(this);
    }

    @Override
    public void visit(FunctionDeclaration node) {
        System.out.println("funcdec");
        //Not checking functions at the moment, just going to statSeq
        currentFunction = node.funcSym();
        node.body().accept(this);
    }

    @Override
    public void visit(DeclarationList node) {
        System.out.println("dec list");
        if(node.empty()) return;
        //Visit all the function declarations
        for(Declaration d: node.declarationList()){
            if(d instanceof FunctionDeclaration){
                FunctionDeclaration temp = (FunctionDeclaration) d;
                d.accept(this);
            }
        }
    }

    private void generateFunctionList(DeclarationList functionDecList){
        functionList.add(new FuncType())
        if(node.empty()) return;
        //Visit all the function declarations
        for(Declaration d: node.declarationList()){
            if(d instanceof FunctionDeclaration){
                FunctionDeclaration temp = (FunctionDeclaration) d;
                d.accept(this);
            }
        }
    }

    @Override
    public void visit (Computation node) {
        //Don't need to error check vars declarations list, but will need to visit functions
        System.out.println("comp");
        if(node.functions() != null){
            node.functions().accept(this);
        }
        node.mainStatementSequence().accept(this);
    }

    public boolean check(AST ast){
        ast.getRoot().accept(this);
        System.out.println(hasError());
        System.out.println(errorReport());

        return hasError();

        //return hasError();

    }
}
