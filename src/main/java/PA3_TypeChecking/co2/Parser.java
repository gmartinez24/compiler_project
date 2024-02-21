package co2;

import java.io.*;
import java.util.*;

public class Parser {

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

// Parser ============================================================
    private co2.Scanner scanner;
    private Token currentToken;

    private BufferedReader reader;
    private StringTokenizer st;

    // TODO: add maps from Token IDENT to int/float/bool

    public Parser (co2.Scanner scanner, InputStream in) {
        this.scanner = scanner;
        currentToken = this.scanner.next();

        reader = new BufferedReader(new InputStreamReader(in));
        st = null;
    }

    public void parse () {
        try {
            computation();
        }
        catch (QuitParseException q) {
            // too verbose
            // errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            // errorBuffer.append("[Could not complete parsing.]");
        }
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

// Pre-defined Functions ======================================================
    private String nextInput () {
        while (st == null || !st.hasMoreElements()) {
            try {
                st = new StringTokenizer(reader.readLine());
            }
            catch (IOException e) {
                throw new QuitParseException("Interepter: Couldn't read data file\n" + e.getMessage());
            }
        }
        return st.nextToken();
    }

    private int readInt () {
        System.out.print("int? ");
        return Integer.parseInt(nextInput());
    }

    private float readFloat () {
        System.out.print("float? ");
        return Float.parseFloat(nextInput());
    }

    private boolean readBool () {
        System.out.print("true or false? ");
        return Boolean.parseBoolean(nextInput());
    }

    private void printInt (int x) {
        System.out.print(x + " ");
    }

    private void printFloat (float x) {
        System.out.printf("%.2f ",x);
    }

    private void printBool (boolean x) {
        System.out.print(x + " ");
    }

    private void println () {
        System.out.println();
    }

// Grammar Rules ==============================================================

    // function for matching rule that only expects nonterminal's FIRST set
    private Token matchNonTerminal (NonTerminal nt) {
        return expectRetrieve(nt);
    }

    // TODO: implement operators and type grammar rules

    // literal = integerLit | floatLit
    private Token literal () {
        return matchNonTerminal(NonTerminal.LITERAL);
    }

    // designator = ident { "[" relExpr "]" }
    private void designator () {
        int lineNum = lineNumber();
        int charPos = charPosition();

        Token ident = expectRetrieve(Token.Kind.IDENT);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            relExpr();
            accept(Token.Kind.CLOSE_BRACKET);
        }
        // TODO: get designated value from appropriate map from IDENT to value
        
    }

    // TODO: implement remaining grammar rules

    // computation	= "main" {varDecl} {funcDecl} "{" statSeq "}" "."
    private void computation () {

        expect(Token.Kind.MAIN);

        // deal with varDecl 0 or many
        while (have(NonTerminal.VAR_DECL)) {
            varDecl();
        }

        // deal with funcDecl 0 or many
        while (have(NonTerminal.FUNC_DECL)) {
            funcDecl();
        }

        expect(Token.Kind.OPEN_BRACE);
        statSeq();
        expect(Token.Kind.CLOSE_BRACE);
        expect(Token.Kind.PERIOD);
    }

    // varDecl = typeDecl ident {"," ident} ";"
    private void varDecl() {
        typeDecl();
        do {
            expect(Token.Kind.IDENT);
        } while(accept(Token.Kind.COMMA));
        expect(Token.Kind.SEMICOLON);
    }

    // typeDecl = type  { "[" integerLit "]" }
    private void typeDecl() {
        expect(NonTerminal.TYPE_DECL);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expect(Token.Kind.INT_VAL);
            expect(Token.Kind.CLOSE_BRACKET);
        }
//        } else {
//            // may need to change this to reflect something other than expecting INT
//            expect(Token.Kind.INT);
//        }


    }

    // funcDecl = "function" ident formalParam ":" ("void" | type ) funcBody
    private void funcDecl() {
        expect(Token.Kind.FUNC);
        expect(Token.Kind.IDENT);
        formalParam();
        expect(Token.Kind.COLON);

        // searches for either a VOID or a TYPE
        if (!(accept(Token.Kind.VOID) || accept(NonTerminal.TYPE_DECL))) {
            // may need to change this to reflect something other than expecting VOID
            expect(NonTerminal.FUNC_DECL);
        }

        funcBody();
    }

    // formalParam = "(" [paramDecl { "," paramDecl}] ")"
    private void formalParam() {
        expect(Token.Kind.OPEN_PAREN);
        if (have(NonTerminal.PARAM_DECL)){
            do {
                paramDecl();
            } while(accept(Token.Kind.COMMA));
        }
        expect(Token.Kind.CLOSE_PAREN);

    }

    // paramDecl = paramType ident
    private void paramDecl() {
        paramType();
        expect(Token.Kind.IDENT);
    }

    // paramType = type {"[" "]"}
    private void paramType() {
        expect(NonTerminal.TYPE_DECL);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expect(Token.Kind.CLOSE_BRACKET);
        }
    }

    // funcBody = "{" {varDecl}  statSeq "}" ";"
    private void funcBody() {
        expect(Token.Kind.OPEN_BRACE);
        while (have(NonTerminal.VAR_DECL)) {
            varDecl();
        }

        statSeq();

        expect(Token.Kind.CLOSE_BRACE);
        expect(Token.Kind.SEMICOLON);
    }

    // statSeq = statement ";" { statement ";" }
    private void statSeq() {
        do{
            statement();
            expect(Token.Kind.SEMICOLON);
        } while (have(NonTerminal.STATEMENT));

    }

    // statement = assign | funcCall | ifStat | whileStat | repeatStat | returnStat
    private void statement() {
        // assign throws an error on erroneous function call (function call without "call" keyword)
        if (have(NonTerminal.ASSIGN)) {
            assign();
        }
        else if (have(NonTerminal.FUNC_CALL)) {
            funcCall();
        }
        else if (have(NonTerminal.IF_STAT)) {
            ifStat();
        }
        else if (have(NonTerminal.WHILE_STAT)) {
            whileStat();
        }
        else if (have(NonTerminal.REPEAT_STAT)) {
            repeatStat();
        }
        else if (have(NonTerminal.RETURN_STAT)) {
            returnStat();
        } else {
            expect(NonTerminal.STATEMENT);
        }

    }

    // assign = designator ( ( assignOp relExpr ) | unaryOp )
    private void assign() {
       designator();
        if (accept(NonTerminal.ASSIGN_OP)) {
            relExpr();
        } else if (accept(NonTerminal.UNARY_OP)) {
            return;
        } else {
            expect(NonTerminal.ASSIGN);
        }
    }

    // relExpr = addExpr { relOp addExpr }
    private void relExpr () {
        // for empty return statements
        if(have(Token.Kind.SEMICOLON) || have(Token.Kind.CLOSE_PAREN)){
            return;
        }
        addExpr();
        while (accept(NonTerminal.REL_OP)){
            addExpr();
        }
    }

    // addExpr = multExpr { addOp multExpr }
    private void addExpr () {
        multExpr();
        while (accept(Token.Kind.ADD) || accept(Token.Kind.SUB) || accept(Token.Kind.OR)) {
            multExpr();
        }
    }

    // multExpr = powExpr { multOp powExpr }
    private void multExpr() {
        powExpr();
        while (accept(Token.Kind.MUL) || accept(Token.Kind.DIV) || accept(Token.Kind.MOD) || accept(Token.Kind.AND)) {
            powExpr();
        }
    }

    // powExpr = groupExpr { powOp groupExpr }
    private void powExpr () {
        groupExpr();
        while (accept(Token.Kind.POW)) {
            groupExpr();
        }
    }

    // groupExpr = literal | designator | "not" relExpr | relation | funcCall
    private void groupExpr() {
        if (accept(NonTerminal.LITERAL)) {
            //System.out.println("lit" + lineNumber());
            return;
        } else if (have(NonTerminal.DESIGNATOR)) {
           // System.out.println("des" + lineNumber());
            designator();
        } else if (accept(Token.Kind.NOT)) {
           // System.out.println("relExp" + lineNumber());
            relExpr();
        } else if (have(Token.Kind.OPEN_PAREN)) {
            //System.out.println("realtion" + lineNumber());
            relation();
        } else if (have(NonTerminal.FUNC_CALL)){
            //System.out.println("call" + lineNumber());
            funcCall();
        } else {
            expect(NonTerminal.GROUP_EXPR);
        }
    }

    // relation = "(" relExpr ")"
    private void relation() {
        expect(Token.Kind.OPEN_PAREN);
        relExpr();
        expect(Token.Kind.CLOSE_PAREN);
    }

    // funCall = "call" ident "(" [ relExpr { "," relExpr } ] ")"
    private void funcCall() {
        expect(Token.Kind.CALL);
        expect(Token.Kind.IDENT);
        expect(Token.Kind.OPEN_PAREN);
        relExpr();
        while (accept(Token.Kind.COMMA)) {
            relExpr();
        }
        expect(Token.Kind.CLOSE_PAREN);
    }

    // ifStat = "if" relation "then" statSeq [ "else" statSeq ] "fi"
    private void ifStat() {
        expect(Token.Kind.IF);
        relation();
        expect(Token.Kind.THEN);
        statSeq();
        if (accept(Token.Kind.ELSE)) {
            statSeq();
        }
        expect(Token.Kind.FI);
    }

    // whileStat = "while" relation "do" statSeq "od"
    private void whileStat () {
        expect(Token.Kind.WHILE);
        relation();
        expect(Token.Kind.DO);
        statSeq();
        expect(Token.Kind.OD);
    }

    // repeatStat = "repeat" statSeq "until" relation
    private void repeatStat () {
        expect(Token.Kind.REPEAT);
        statSeq();
        expect(Token.Kind.UNTIL);
        relation();
    }

    // returnStat = "return" [ relExpr ]
    private void returnStat () {
        expect(Token.Kind.RETURN);
        relExpr();
    }

}
