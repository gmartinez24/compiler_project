package co2;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import ast.*;
import types.TypeList;
import types.*;

import static java.lang.Integer.parseInt;

public class Compiler {

    // Error Reporting ============================================================
    private StringBuilder errorBuffer = new StringBuilder();

    private String reportSyntaxError (NonTerminal nt) {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected a token from " + nt.name() + " but got " + currentToken.kind + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }

    private String reportSyntaxError (Token.Kind kind) {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + kind + " but got " + currentToken.kind + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }

    public String errorReport () {
        return errorBuffer.toString();
    }

    public boolean hasError () {
        return errorBuffer.length() != 0;
    }

    private class QuitParseException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public QuitParseException (String errorMessage) {
            super(errorMessage);
        }
    }

    private int lineNumber () {
        return currentToken.lineNumber();
    }

    private int charPosition () {
        return currentToken.charPosition();
    }

    // Compiler ===================================================================
    private Scanner scanner;
    private Token currentToken;

    private int numDataRegisters; // available registers are [1..numDataRegisters]
    private List<Integer> instructions;

    // Need to map from IDENT to memory offset

    public Compiler (Scanner scanner, int numRegs) {
        this.scanner = scanner;
        currentToken = this.scanner.next();
        numDataRegisters = numRegs;
        instructions = new ArrayList<>();
    }

    //TODO
    public AST genAST() {
        // the computation function returns an instance of computation to pass
        // to constructor of AST as root node
        initSymbolTable();
        return new AST(computation());
    }
    
    public void interpret() {
        initSymbolTable();
        try {
            computation();
        }
        catch (QuitParseException q) {
            // too verbose
            // errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            // errorBuffer.append("[Could not complete parsing.]");
        }
    }

    public int[] compile () {
        initSymbolTable();
        try {
            computation();
            return instructions.stream().mapToInt(Integer::intValue).toArray();
        }
        catch (QuitParseException q) {
            // errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            // errorBuffer.append("[Could not complete parsing.]");
            return new ArrayList<Integer>().stream().mapToInt(Integer::intValue).toArray();
        }
    }

    // SymbolTable Management =====================================================
    private SymbolTable symbolTable;

    private void initSymbolTable () {
        symbolTable = new SymbolTable();
        //throw new RuntimeException("implement initSymbolTable");
    }

    private void enterScope () {
        symbolTable.enterScope();
        //throw new RuntimeException("implement enterScope");
    }

    private void exitScope () {
        symbolTable.exitScope();
        //throw new RuntimeException("implement exitScope");
    }

    private Symbol tryResolveVariable (Token ident) {
        try {
            return symbolTable.lookup(ident.lexeme());
        } catch(SymbolNotFoundError e) {
            reportResolveSymbolError(ident.lexeme(), ident.lineNumber(), ident.charPosition());
            return null;
        }
    }

    private Symbol tryDeclareVariable (Symbol sym) {
        try {
            return symbolTable.insert(sym);
        } catch (RedeclarationError e) {
            reportDeclareSymbolError(sym.name(), sym.lineNumber(), sym.charPosition());
            return null;
        }
    }

    private String reportResolveSymbolError (String name, int lineNum, int charPos) {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }

    private String reportDeclareSymbolError (String name, int lineNum, int charPos) {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        return message;
    }


    // Helper Methods =============================================================
    private boolean have (Token.Kind kind) {
        return currentToken.is(kind);
    }

    private boolean have (NonTerminal nt) {
        return nt.firstSet().contains(currentToken.kind);
    }

    private boolean accept (Token.Kind kind) {
        if (have(kind)) {
            try {
                currentToken = scanner.next();
            }
            catch (NoSuchElementException e) {
                if (!kind.equals(Token.Kind.EOF)) {
                    String errorMessage = reportSyntaxError(kind);
                    throw new QuitParseException(errorMessage);
                }
            }
            return true;
        }
        return false;
    }

    private boolean accept (NonTerminal nt) {
        if (have(nt)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }

    private boolean expect (Token.Kind kind) {
        if (accept(kind)) {
            return true;
        }
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
    }

    private boolean expect (NonTerminal nt) {
        if (accept(nt)) {
            return true;
        }
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
    }

    private Token expectRetrieve (Token.Kind kind) {
        Token tok = currentToken;
        if (accept(kind)) {
            return tok;
        }
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
    }

    private Token expectRetrieve (NonTerminal nt) {
        Token tok = currentToken;
        if (accept(nt)) {
            return tok;
        }
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
    }


// Grammar Rules ==============================================================

    // function for matching rule that only expects nonterminal's FIRST set
    private Token matchNonTerminal (NonTerminal nt) {
        return expectRetrieve(nt);
    }


    // literal = integerLit | floatLit
    private Token literal () {
        return matchNonTerminal(NonTerminal.LITERAL);
    }


    // designator = ident { "[" relExpr "]" }
    // unsure of how to implement this. what to return here
    // see that ArrayIndex would be used here, but how to return identifier?
    private Identifier designator () {
//        int lineNum = lineNumber();
//        int charPos = charPosition();

        Token tok = expectRetrieve(Token.Kind.IDENT);
        int lineNum = tok.lineNumber();
        int charPos = tok.charPosition();

        List<Expression> indexes = new ArrayList<>();
        while (accept(Token.Kind.OPEN_BRACKET)) {
            indexes.add(relExpr());
            accept(Token.Kind.CLOSE_BRACKET);
        }

        // if array is null
        //ArrayIndex array = new ArrayIndex(lineNum, charPos, exps);
        // use previous entry in symbol table to ge type
        Symbol sym = tryResolveVariable(tok);
        return new Identifier(lineNum, charPos, sym, indexes);
    }


    // computation	= "main" {varDecl} {funcDecl} "{" statSeq "}" "."
    private Computation computation () {

        // create a type list as the type for main
        Token main = expectRetrieve(Token.Kind.MAIN);
        TypeList mainType = new TypeList();
        mainType.append(new VoidType());
        Symbol mainSymbol = new Symbol(mainType, main.lexeme(), main.lineNumber(), main.charPosition());

        DeclarationList varList = null;
        DeclarationList funcList = null;
        StatementSequence statementSequence;

        if (have(NonTerminal.VAR_DECL)) {
            // deal with varDecl 0 or many
            varList = new DeclarationList(currentToken.lineNumber(), currentToken.charPosition());
            while (have(NonTerminal.VAR_DECL)) {
                varDecl(varList);
            }
        }

        if (have(NonTerminal.FUNC_DECL)) {
            // deal with funcDecl 0 or many
            funcList = new DeclarationList(currentToken.lineNumber(), currentToken.charPosition());
            while (have(NonTerminal.FUNC_DECL)) {
                FunctionDeclaration funcDec = funcDecl();
                tryDeclareVariable(funcDec.funcSym());
                funcList.addDeclaration(funcDec);

            }
        }

        expect(Token.Kind.OPEN_BRACE);
        statementSequence = statSeq();
        expect(Token.Kind.CLOSE_BRACE);
        expect(Token.Kind.PERIOD);

        return new Computation(main.lineNumber(), main.charPosition(), mainSymbol, varList, funcList, statementSequence);
    }

    // varDecl = typeDecl ident {"," ident} ";"
    private void varDecl(DeclarationList varList) {
        Type type = typeDecl();
        do {
            Token var = expectRetrieve(Token.Kind.IDENT);
            Symbol sym = new Symbol(type, var.lexeme(), var.lineNumber(), var.charPosition());
            tryDeclareVariable(sym);
            varList.addDeclaration(new VariableDeclaration(var.lineNumber(), var.charPosition(), type, var.lexeme()));
        } while(accept(Token.Kind.COMMA));
        expect(Token.Kind.SEMICOLON);
    }

    // typeDecl = type  { "[" integerLit "]" }
    private Type typeDecl() {
        Token tok = expectRetrieve(NonTerminal.TYPE_DECL);
        Type type = null;
        if (have(Token.Kind.OPEN_BRACKET)) {
            int dimensionSize;
            List<Integer> dimensions = new ArrayList<>();
            while (accept(Token.Kind.OPEN_BRACKET)) {
                dimensionSize = parseInt(expectRetrieve(Token.Kind.INT_VAL).lexeme());
                dimensions.add(dimensionSize);
                expect(Token.Kind.CLOSE_BRACKET);
            }
            // getting type of array elements
            Type elementType;
            if (tok.is(Token.Kind.INT)) {
                elementType = new IntType();
            } else if (tok.is(Token.Kind.FLOAT)) {
                elementType = new FloatType();
            } else {
                elementType = new BoolType();
            }
            type = new ArrayType(elementType, dimensions);

        } else if (tok.is(Token.Kind.INT)) {
            type = new IntType();
        } else if (tok.is(Token.Kind.FLOAT)) {
            type = new FloatType();
        } else if (tok.is(Token.Kind.BOOL)) {
            type = new BoolType();
        }

        return type;
    }

    // funcDecl = "function" ident formalParam ":" ("void" | type ) funcBody
    private FunctionDeclaration funcDecl() {

        enterScope();

        Token func = expectRetrieve(Token.Kind.FUNC);
        String name = expectRetrieve(Token.Kind.IDENT).lexeme();

        TypeList funcType = new TypeList();

        List<Symbol> params= formalParam();
        for (Symbol param : params) {
            funcType.append(param.type());
        }
        expect(Token.Kind.COLON);

        // searches for either a VOID or a TYPE
        Token.Kind returnType = currentToken.kind();
        if (accept(Token.Kind.VOID) || accept(NonTerminal.TYPE_DECL)) {
            if (returnType == Token.Kind.VOID) {
                funcType.append(new VoidType());
            } else if (returnType == Token.Kind.INT) {
                funcType.append(new IntType());
            } else if (returnType == Token.Kind.FLOAT) {
                funcType.append(new FloatType());
            } else if (returnType == Token.Kind.BOOL) {
                funcType.append(new BoolType());
            }

        } else {
            expect(NonTerminal.FUNC_DECL);
        }

        FunctionBody functionBody = funcBody();

        exitScope();

        return new FunctionDeclaration(func.lineNumber(), func.charPosition(), funcType, name, params, functionBody);
    }

    // formalParam = "(" [paramDecl { "," paramDecl}] ")"
    private List<Symbol> formalParam() {

        List<Symbol> params = new ArrayList<>();
        expect(Token.Kind.OPEN_PAREN);
        if (have(NonTerminal.PARAM_DECL)){
            do {
                Symbol param = paramDecl();
                tryDeclareVariable(param);
                params.add(param);
            } while(accept(Token.Kind.COMMA));
        }
        expect(Token.Kind.CLOSE_PAREN);
        return params;
    }

    // paramDecl = paramType ident
    private Symbol paramDecl() {
        Type paramType = paramType();
        Token paramName = expectRetrieve(Token.Kind.IDENT);
        return new Symbol(paramType, paramName.lexeme(), paramName.lineNumber(), paramName.charPosition());
    }

    // paramType = type {"[" "]"}
    private Type paramType() {
        Token tok = expectRetrieve(NonTerminal.TYPE_DECL);
        Type type;
        if (have(Token.Kind.OPEN_BRACKET)) {
            // only making this an array list to match constructor for ArrayType
            List<Integer> dimensions = new ArrayList<>();
            while (accept(Token.Kind.OPEN_BRACKET)) {
                dimensions.add(0);
                expect(Token.Kind.CLOSE_BRACKET);
            }

            Type elementType;
            if (tok.is(Token.Kind.INT)) {
                elementType = new IntType();
            } else if (tok.is(Token.Kind.FLOAT)) {
                elementType = new FloatType();
            } else {
                elementType = new BoolType();
            }

            type = new ArrayType(elementType, dimensions);
        } else if (tok.is(Token.Kind.INT)) {
            type = new IntType();
        } else if (tok.is(Token.Kind.FLOAT)) {
            type = new FloatType();
        } else {
            type = new BoolType();
        }

        return type;
    }

    // funcBody = "{" {varDecl}  statSeq "}" ";"
    private FunctionBody funcBody() {
        expect(Token.Kind.OPEN_BRACE);

        DeclarationList varList = null;
        int lineNum = currentToken.lineNumber();
        int charPosition = currentToken.charPosition();
        if (have(NonTerminal.VAR_DECL)) {
            // deal with varDecl 0 or many
            varList = new DeclarationList(currentToken.lineNumber(), currentToken.charPosition());
            while (have(NonTerminal.VAR_DECL)) {
                varDecl(varList);
            }
        }

        StatementSequence statementSequence = statSeq();

        expect(Token.Kind.CLOSE_BRACE);
        expect(Token.Kind.SEMICOLON);

        return new FunctionBody(lineNum,charPosition,varList,statementSequence);
    }

    // statSeq = statement ";" { statement ";" }
    private StatementSequence statSeq() {
        StatementSequence statementSequence = new StatementSequence(currentToken.lineNumber(), currentToken.charPosition());
        do{
            statementSequence.addStatement(statement());
            expect(Token.Kind.SEMICOLON);
        } while (have(NonTerminal.STATEMENT));
        return statementSequence;
    }

    // statement = assign | funcCall | ifStat | whileStat | repeatStat | returnStat
    private Statement statement() {
        // assign throws an error on erroneous function call (function call without "call" keyword)
        if (have(NonTerminal.ASSIGN)) {
            return assign();
        }
        else if (have(NonTerminal.FUNC_CALL)) {
            return funcCall();
        }
        else if (have(NonTerminal.IF_STAT)) {
            return ifStat();
        }
        else if (have(NonTerminal.WHILE_STAT)) {
            return whileStat();
        }
        else if (have(NonTerminal.REPEAT_STAT)) {
            return repeatStat();
        }
        else if (have(NonTerminal.RETURN_STAT)) {
            return returnStat();
        } else {
            expect(NonTerminal.STATEMENT);
            return null;
        }

    }

    // assign = designator ( ( assignOp relExpr ) | unaryOp )
    private Assignment assign() {
        int lineNumber = currentToken.lineNumber();
        int charPosition = currentToken.charPosition();
        Expression leftSide = null;
        Expression rightSide = null;
        leftSide = designator();
        if (accept(NonTerminal.ASSIGN_OP)) {
            rightSide = relExpr();
            return new Assignment(lineNumber, charPosition, leftSide, rightSide);
        } else if (have(NonTerminal.UNARY_OP)) {
            // hard code ++ and --
            String unaryOP = expectRetrieve(NonTerminal.UNARY_OP).lexeme();
            if (unaryOP == "++") {
                rightSide = new Addition(lineNumber, charPosition, leftSide, new IntegerLiteral(lineNumber, charPosition, "1"));
                return new Assignment(lineNumber, charPosition, leftSide, rightSide);
            } else {
                rightSide = new Subtraction(lineNumber, charPosition, leftSide, new IntegerLiteral(lineNumber, charPosition, "1"));
                return new Assignment(lineNumber, charPosition, leftSide, rightSide);
            }

        } else {
            expect(NonTerminal.ASSIGN);
            return null;
        }
        //return new Assignment(lineNumber, charPosition, leftSide, rightSide);

    }

    // relExpr = addExpr { relOp addExpr }
    private Expression relExpr () {
        // for empty return statements
        if(have(Token.Kind.SEMICOLON) || have(Token.Kind.CLOSE_PAREN)){
            //Need to figure out what to do with return of NULL
            return null;
        }
        Expression rightSide;
        Expression leftSide = addExpr();
        while (have(NonTerminal.REL_OP)){
            Token tok = expectRetrieve(NonTerminal.REL_OP);
            rightSide = addExpr();
            leftSide = new Relation(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide, tok);
        }
        return leftSide;
    }

    // addExpr = multExpr { addOp multExpr }
    private Expression addExpr () {
        Expression rightSide;
        Expression leftSide = multExpr();
        while (have(Token.Kind.ADD) || have(Token.Kind.SUB) || have(Token.Kind.OR)) {
            if (accept(Token.Kind.ADD)) {
                rightSide = multExpr();
                leftSide = new Addition(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide);
            } else if (accept(Token.Kind.SUB)) {
                rightSide = multExpr();
                leftSide = new Subtraction(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide);
            } else if (accept(Token.Kind.OR)) {
                rightSide = multExpr();
                leftSide = new LogicalOr(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide);
            }
        }
        return leftSide;
    }

    // multExpr = powExpr { multOp powExpr }
    private Expression multExpr() {
        Expression rightSide;
        Expression leftSide = powExpr();
        while (have(Token.Kind.MUL) || have(Token.Kind.DIV) || have(Token.Kind.MOD) || have(Token.Kind.AND)) {
            if (accept(Token.Kind.MUL)){
                rightSide = powExpr();
                leftSide = new Multiplication(leftSide.lineNumber(), leftSide.charPosition(),leftSide, rightSide);
            } else if (accept(Token.Kind.DIV)){
                rightSide = powExpr();
                leftSide = new Division(leftSide.lineNumber(), leftSide.charPosition(),leftSide, rightSide);
            } else if (accept(Token.Kind.MOD)) {
                rightSide = powExpr();
                leftSide = new Modulo(leftSide.lineNumber(), leftSide.charPosition(),leftSide, rightSide);
            } else if (accept(Token.Kind.AND)) {
                rightSide = powExpr();
                leftSide = new LogicalAnd(leftSide.lineNumber(), leftSide.charPosition(),leftSide, rightSide);
            }
        }

        return leftSide;
    }

    // powExpr = groupExpr { powOp groupExpr }
    private Expression powExpr () {
        //List<Expression> rightSide = new ArrayList<>();
        Expression rightSide;
        Expression leftSide = groupExpr();
        while (accept(Token.Kind.POW)) {
            ///rightSide.add(groupExpr());
            rightSide = groupExpr();
            leftSide = new Power(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide);
        }
//        if (!rightSide.isEmpty()) {
//            return new Power(leftSide.lineNumber(), leftSide.charPosition, leftSide, rightSide);
//        }
        return leftSide;
    }

    // groupExpr = literal | designator | "not" relExpr | relation | funcCall
    private Expression groupExpr() {
        if (have(NonTerminal.LITERAL)) {
            Token tok = expectRetrieve(NonTerminal.LITERAL);
            //System.out.println("lit" + lineNumber());
            if (tok.is(Token.Kind.TRUE) || tok.is(Token.Kind.FALSE)){
                return new BoolLiteral(tok.lineNumber(), tok.charPosition(), tok.lexeme());
            }
            if (tok.is(Token.Kind.INT_VAL)) {
                return new IntegerLiteral(tok.lineNumber(), tok.charPosition(), tok.lexeme());
            }
            if (tok.is(Token.Kind.FLOAT_VAL)) {
                return new FloatLiteral(tok.lineNumber(), tok.charPosition(), tok.lexeme());
            }
        } else if (have(NonTerminal.DESIGNATOR)) {
            // System.out.println("des" + lineNumber());
            return designator();
        } else if (have(Token.Kind.NOT)) {
            // System.out.println("relExp" + lineNumber());
            Token tok = expectRetrieve(Token.Kind.NOT);
            return new LogicalNot(tok.lineNumber(), tok.charPosition(), relExpr());
            //return relExpr();
        } else if (have(Token.Kind.OPEN_PAREN)) {
            //System.out.println("realtion" + lineNumber());
            return relation();
        } else if (have(NonTerminal.FUNC_CALL)){
            //System.out.println("call" + lineNumber());
            return funcCall();
        } else {
            expect(NonTerminal.GROUP_EXPR);

        }
        return null;
    }

    // relation = "(" relExpr ")"
    private Expression relation() {
        expect(Token.Kind.OPEN_PAREN);
        Expression relation = relExpr();
        expect(Token.Kind.CLOSE_PAREN);
        return relation;
    }

    // funCall = "call" ident "(" [ relExpr { "," relExpr } ] ")"
    private FunctionCall funcCall() {
        Token tok = expectRetrieve(Token.Kind.CALL);
        Token ident = expectRetrieve(Token.Kind.IDENT);
        Symbol funcSym = tryResolveVariable(ident);

        expect(Token.Kind.OPEN_PAREN);

        List<Expression> args = new ArrayList<>();
        System.out.println(currentToken);
        Expression firstArg = relExpr();
        ArgumentList argumentList;
        if(firstArg == null){
            argumentList = new ArgumentList(lineNumber(), charPosition(), args);
        }
        else{
            args.add(firstArg);
            while (accept(Token.Kind.COMMA)) {
                args.add(relExpr());
            }
            argumentList = new ArgumentList(firstArg.lineNumber(), firstArg.charPosition(), args);
        }


        expect(Token.Kind.CLOSE_PAREN);



        return new FunctionCall(tok.lineNumber(), tok.charPosition(), funcSym, argumentList);
    }

    // ifStat = "if" relation "then" statSeq [ "else" statSeq ] "fi"
    private IfStatement ifStat() {
        enterScope();

        Token ifStat = expectRetrieve(Token.Kind.IF);

        Expression rel = relation();
        expect(Token.Kind.THEN);
        StatementSequence thenBlock = statSeq();
        StatementSequence elseBlock;
        if (accept(Token.Kind.ELSE)) {
            elseBlock = statSeq();
        }
        else{
            elseBlock = new StatementSequence(lineNumber(), charPosition());
        }
        expect(Token.Kind.FI);

        exitScope();

        return new IfStatement(ifStat.lineNumber(), ifStat.charPosition(), rel, thenBlock, elseBlock);
    }

    // whileStat = "while" relation "do" statSeq "od"
    private WhileStatement whileStat () {
        enterScope();

        Token whileStat = expectRetrieve(Token.Kind.WHILE);
        Expression rel = relation();
        expect(Token.Kind.DO);
        StatementSequence doBlock = statSeq();
        expect(Token.Kind.OD);

        exitScope();

        return new WhileStatement(whileStat.lineNumber(), whileStat.charPosition(), rel, doBlock);
    }

    // repeatStat = "repeat" statSeq "until" relation
    private RepeatStatement repeatStat () {

        enterScope();

        Token repeatStat = expectRetrieve(Token.Kind.REPEAT);
        StatementSequence repeatBlock = statSeq();
        expect(Token.Kind.UNTIL);
        Expression rel = relation();

        exitScope();

        return new RepeatStatement(repeatStat.lineNumber(), repeatStat.charPosition(), repeatBlock, rel);
    }

    // returnStat = "return" [ relExpr ]
    private ReturnStatement returnStat () {
        Token tok = expectRetrieve(Token.Kind.RETURN);
        Expression rel = relExpr();
        return new ReturnStatement(tok.lineNumber(), tok.charPosition(), rel);
    }



}
