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
        do  {
            expect(Token.Kind.IDENT);
        } while(accept(Token.Kind.COMMA));
        expect(Token.Kind.SEMICOLON);
    }

    // typeDecl = type  { "[" integerLit "]" }
    private void typeDecl() {
        if (accept(Token.Kind.INT) | accept(Token.Kind.BOOL) | accept(Token.Kind.FLOAT)) {
            while (accept(Token.Kind.OPEN_BRACKET)) {
                expect(Token.Kind.INT_VAL);
                expect(Token.Kind.CLOSE_BRACKET);
            }
        } else {
            // may need to change this to reflect something other than expecting INT
            expect(Token.Kind.INT);
        }


    }

    // funcDecl = "function" ident formalParam ":" ("void" | type ) funcBody
    private void funcDecl() {
        expect(Token.Kind.FUNC);
        expect(Token.Kind.IDENT);
        formalParam();
        expect(Token.Kind.COLON);

        // searches for either a VOID or a TYPE
        if (accept(Token.Kind.VOID) | accept(Token.Kind.INT) | accept(Token.Kind.BOOL) | accept(Token.Kind.FLOAT)) {
            return;
        } else {
            // may need to change this to reflect something other than expecting VOID
            expect(Token.Kind.VOID);
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

    private void statSeq() {

    }


    private void paramDecl() {}

}
