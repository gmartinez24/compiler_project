package co2;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import ast.*;

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
        throw new RuntimeException("implement initSymbolTable");
    }

    private void enterScope () {
        throw new RuntimeException("implement enterScope");
    }

    private void exitScope () {
        throw new RuntimeException("implement exitScope");
    }

    private Symbol tryResolveVariable (Token ident) {
        //TODO: Try resolving variable, handle SymbolNotFoundError
    }

    private Symbol tryDeclareVariable (Token ident) {
        //TODO: Try declaring variable, handle RedeclarationError
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
    private Expression designator () {
        int lineNum = lineNumber();
        int charPos = charPosition();

        String ident = expectRetrieve(Token.Kind.IDENT).lexeme();
        List<Expression> exps = new ArrayList<>();
        while (accept(Token.Kind.OPEN_BRACKET)) {
            exps.add(relExpr());
            accept(Token.Kind.CLOSE_BRACKET);
        }

        // if array is null
        ArrayIndex array = new ArrayIndex(lineNum, charPos, ident, exps);

        return Identifier(lineNum, charPos, ident, array);
    }


    // computation	= "main" {varDecl} {funcDecl} "{" statSeq "}" "."
    private Computation computation () {

        Token main = expectRetrieve(Token.Kind.MAIN);
        Symbol mainSymbol = new Symbol("TypeList() -> void", "main");
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
                funcList.addDeclaration(funcDecl());
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
        String type = typeDecl();
        do {
            Token var = expectRetrieve(Token.Kind.IDENT);
            varList.addDeclaration(new VariableDeclaration(var.lineNumber(), var.charPosition(), type, var.lexeme()));
        } while(accept(Token.Kind.COMMA));
        expect(Token.Kind.SEMICOLON);
    }

    // typeDecl = type  { "[" integerLit "]" }
    private String typeDecl() {
        String type = expectRetrieve(NonTerminal.TYPE_DECL).lexeme();
        while (accept(Token.Kind.OPEN_BRACKET)) {
            type +="[";
            type+=expectRetrieve(Token.Kind.INT_VAL).lexeme();
            expect(Token.Kind.CLOSE_BRACKET);
            type+="]";
        }
        return type;
    }

    // funcDecl = "function" ident formalParam ":" ("void" | type ) funcBody
    private FunctionDeclaration funcDecl() {
        Token func = expectRetrieve(Token.Kind.FUNC);
        String name = expectRetrieve(Token.Kind.IDENT).lexeme();
        String type = "TypeList(";
        List<Symbol> params= formalParam();
        for (Symbol param : params) {
            type += param.type()+ ", ";
        }
        // remove last comma and space
        type = type.substring(0, type.length() - 2);
        //type += formalParam() + ")->"+ currentToken.lexeme();
        expect(Token.Kind.COLON);

        // searches for either a VOID or a TYPE
        if (!(accept(Token.Kind.VOID) || accept(NonTerminal.TYPE_DECL))) {
            // may need to change this to reflect something other than expecting VOID
            expect(NonTerminal.FUNC_DECL);
        }

        FunctionBody functionBody = funcBody();
        return new FunctionDeclaration(func.lineNumber(), func.charPosition(), type, name, params, functionBody);
    }

    // formalParam = "(" [paramDecl { "," paramDecl}] ")"
    private List<Symbol> formalParam() {
//        String params = "";
        List<Symbol> params = new ArrayList<>();
        expect(Token.Kind.OPEN_PAREN);
        if (have(NonTerminal.PARAM_DECL)){
            do {
//                params += paramDecl() + " ";
                params.add(paramDecl());
            } while(accept(Token.Kind.COMMA));
//            params = params.substring(0, params.length() - 1);
        }
        expect(Token.Kind.CLOSE_PAREN);
        return params;
    }

    // paramDecl = paramType ident
    private Symbol paramDecl() {
        String paramType = paramType();
        String paramName = expectRetrieve(Token.Kind.IDENT).lexeme();
        Symbol param = new Symbol(paramType, paramName);
        return param;
    }

    // paramType = type {"[" "]"}
    private String paramType() {
        String paramType = expectRetrieve(NonTerminal.TYPE_DECL).lexeme();
        while (accept(Token.Kind.OPEN_BRACKET)) {
            paramType+="[]";
            expect(Token.Kind.CLOSE_BRACKET);
        }
        return paramType;
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
        StatementSequence statementSequence = null;
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
    private Statement assign() {
        int lineNumber = currentToken.lineNumber();
        int charPosition = currentToken.charPosition();
        Expression leftSide = null;
        Expression rightSide = null;
        leftSide = designator();
        if (accept(NonTerminal.ASSIGN_OP)) {
            rightSide = relExpr();
            return new Assignment(lineNumber, charPosition, leftSide, rightSide);
        } else if (have(NonTerminal.UNARY_OP)) {
            String unaryOP = expectRetrieve(NonTerminal.UNARY_OP).lexeme();
            return new Assignment(lineNumber, charPosition, leftSide, unaryOP);
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
            return null;
        }
        Expression rightSide;
        Expression leftSide = addExpr();
        while (have(NonTerminal.REL_OP)){
            Token tok = expectRetrieve(NonTerminal.REL_OP);
            rightSide = addExpr();
            leftSide = new Relation(leftSide.lineNumber(), leftSide.charPosition(), leftSide, rightSide, tok.lexeme());
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
            return LogicalNot(tok.lineNumber(), tok.charPosition(), relExpr());
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
    }

    // relation = "(" relExpr ")"
    private Relation relation() {
        expect(Token.Kind.OPEN_PAREN);
        Relation relation = relExpr();
        expect(Token.Kind.CLOSE_PAREN);
        return relation;
    }

    // funCall = "call" ident "(" [ relExpr { "," relExpr } ] ")"
    private FunctionCall funcCall() {
        Token tok = expectRetrieve(Token.Kind.CALL);
        String ident = expectRetrieve(Token.Kind.IDENT).lexeme;
        expect(Token.Kind.OPEN_PAREN);

        List<Expression> args = new ArrayList<>();
        Expression firstArg = relExpr();
        args.add(firstArg);
        while (accept(Token.Kind.COMMA)) {
            args.add(relExpr());
        }
        ArgumentList argumentList = new ArgumentList(firstArg.lineNumber(), firstArg.charPosition(), args);

        expect(Token.Kind.CLOSE_PAREN);

        return new FunctionCall(tok.lineNumber(), tok.charPosition(), tok.lexeme(), argumentList);
    }

    // ifStat = "if" relation "then" statSeq [ "else" statSeq ] "fi"
    private IfStatement ifStat() {
        Token ifStat = expectRetrieve(Token.Kind.IF);

        Relation rel = relation();
        expect(Token.Kind.THEN);
        StatementSequence thenBlock = statSeq();
        StatementSequence elseBlock = null;
        if (accept(Token.Kind.ELSE)) {
            elseBlock = statSeq();
        }
        expect(Token.Kind.FI);

        return new IfStatement(ifStat.lineNumber(), ifStat.charPosition(), rel, thenBlock, elseBlock);
    }

    // whileStat = "while" relation "do" statSeq "od"
    private WhileStatement whileStat () {
        Token whileStat = expectRetrieve(Token.Kind.WHILE);
        Relation rel = relation();
        expect(Token.Kind.DO);
        StatementSequence doBlock = statSeq();
        expect(Token.Kind.OD);
        return new WhileStatement(whileStat.lineNumber(), whileStat.charPosition(), rel, doBlock);
    }

    // repeatStat = "repeat" statSeq "until" relation
    private void repeatStat () {
        Token repeatStat = expectRetrieve(Token.Kind.REPEAT);
        StatementSequence repeatBlock = statSeq();
        expect(Token.Kind.UNTIL);
        Relation rel = relation();
        return new RepeatStatement(repeatStat.lineNumber(), repeatStat.charPosition(), repeatBlock, rel);
    }

    // returnStat = "return" [ relExpr ]
    private void returnStat () {
        Token tok = expectRetrieve(Token.Kind.RETURN);
        Relation rel = relExpr();
        return new ReturnStatment(tok.lineNumber(), tok.charPosition(), rel);
    }



}
