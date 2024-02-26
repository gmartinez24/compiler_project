package co2;

import java.io.*;
import java.util.*;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;


/*
    Modified from Grant Martinez Parse Implementation
    Design influenced by provided Interpreter.java
 */
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
    private Scanner scanner;
    private Token currentToken;

    private BufferedReader reader;
    private StringTokenizer st;

    // TODO: add maps from Token IDENT to int/float/bool

    HashMap<String, String> intMap = new HashMap<>();
    HashMap<String, String> floatMap = new HashMap<>();
    HashMap<String, String> boolMap = new HashMap<>();

    public Parser(co2.Scanner scanner, InputStream in) {
        this.scanner = scanner;
        currentToken = this.scanner.next();

        reader = new BufferedReader(new InputStreamReader(in));
        st = null;
    }

    public void interpret () {
        try {
            computation();
        }
        catch (QuitParseException q) {
            // too verbose
             errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
             errorBuffer.append("[Could not complete parsing.]");
        }
    }

// Helper Methods =============================================================
    private void insertMap(int map, String name, String value) {
        if (map == 1) {
            intMap.put(name, value);
        } else if (map == 2) {
            floatMap.put(name, value);
        } else if (map == 3) {
            boolMap.put(name, value);
        }
    }

    private boolean isIntLiteral(String expr) {
        try {
            Integer.parseInt(expr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isFloatLiteral(String expr) {
        try {
            Float.parseFloat(expr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isBoolLiteral(String expr) {
        try {
            Boolean.parseBoolean(expr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void assignment(String name, String operator, String expr) {
        String currValue  = "";
        String newVal = "";

        int type = 0;

        // search 3 maps for the name given
        if (intMap.containsKey(name)) {
            type = 1;
            currValue = intMap.get(name);
        } else if (floatMap.containsKey(name)) {
            type = 2;
            currValue = floatMap.get(name);
        } else if (boolMap.containsKey(name)) {
            type = 3;
            currValue = boolMap.get(name);
        }

        if (operator.equals("=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                intMap.put(name, expr);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                floatMap.put(name, expr);
            } else if (type == 3) {
                if (!isBoolLiteral(expr)) {
                    expr = boolMap.get(expr);
                }
                boolMap.put(name, expr);
            }
        } else if (operator.equals("+=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = parseInt(currValue) + parseInt(expr);
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                if (!isFloatLiteral(currValue)) {
                    currValue = floatMap.get(currValue);
                }
                float newFloat = parseFloat(currValue) + parseFloat(expr);
                newVal = Float.toString(newFloat);
                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot add to a boolean type");
            }

        } else if (operator.equals("-=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = parseInt(currValue) - parseInt(expr);
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                if (!isFloatLiteral(currValue)) {
                    currValue = floatMap.get(currValue);
                }
                float newFloat = parseFloat(currValue) - parseFloat(expr);
                newVal = Float.toString(newFloat);
                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot subtract from a boolean type");
            }
        }else if (operator.equals("*=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = parseInt(currValue) * parseInt(expr);
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                if (!isFloatLiteral(currValue)) {
                    currValue = floatMap.get(currValue);
                }
                float newFloat = parseFloat(currValue) * parseFloat(expr);
                newVal = Float.toString(newFloat);
                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot multiply by a boolean type");
            }

        } else if (operator.equals("/=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = parseInt(currValue) / parseInt(expr);
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                if (!isFloatLiteral(currValue)) {
                    currValue = floatMap.get(currValue);
                }
                float newFloat = parseFloat(currValue) / parseFloat(expr);
                newVal = Float.toString(newFloat);
                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot divide by a boolean type");
            }

        } else if (operator.equals("%=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = parseInt(currValue) % parseInt(expr);
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                if (!isFloatLiteral(expr)) {
                    expr = floatMap.get(expr);
                }
                if (!isFloatLiteral(currValue)) {
                    currValue = floatMap.get(currValue);
                }
                float newFloat = parseFloat(currValue) % parseFloat(expr);
                newVal = Float.toString(newFloat);
                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot divide by a boolean type");
            }

        } else if (operator.equals("^=")) {
            if (type == 1) {
                if (!isIntLiteral(expr)) {
                    expr = intMap.get(expr);
                }
                if (!isIntLiteral(currValue)) {
                    currValue = intMap.get(currValue);
                }
                int newInt = (int) Math.pow(parseInt(currValue) ,parseInt(expr));
                newVal = Integer.toString(newInt);
                intMap.put(name, newVal);
            } else if (type == 2) {
                System.out.println("Cannot raise float to a power and return float");
//                if (!isFloatLiteral(expr)) {
//                    expr = floatMap.get(expr);
//                }
//                if (!isFloatLiteral(currValue)) {
//                    currValue = floatMap.get(currValue);
//                }
//                float newFloat = (int) (Math.pow(parseFloat(currValue),  parseFloat(expr)));
//                newVal = Float.toString(newFloat);
//                floatMap.put(name, newVal);
            } else if (type == 3) {
                System.out.println("Cannot raise boolean to a power");
            }
        }


    }

    private void assignmentUnary(String name, String operator) {
        String currValue = "";
        String newValue = "";

        // determine if name is of type float or int
        if (intMap.containsKey(name)) {
            currValue = intMap.get(name);
            int newInt = Integer.parseInt(currValue);
            if (operator.equals("++")) {
                newInt++;
            } else if (operator.equals("--")){
                newInt--;
            }
            newValue = Integer.toString(newInt);
            intMap.put(name, newValue);
        } else if (floatMap.containsKey(name)){
            currValue = floatMap.get(name);
            float newFloat = Float.parseFloat(currValue);
            if (operator.equals("++")) {
                newFloat++;
            } else if (operator.equals("--")){
                newFloat--;
            }
            newValue = Float.toString(newFloat);
            floatMap.put(name, newValue);
        }
    }

    private String runFunc(String name, String arg) {
        if (name.equals("readInt")) {
            return Integer.toString(readInt());
        } else if (name.equals("readFloat")) {
            return Float.toString(readFloat());
        } else if (name.equals("readBool")) {
            return Boolean.toString((readBool()));
        } else if (name.equals("printInt")) {
            if (!isIntLiteral(arg)) {
                arg = intMap.get(arg);
            }
            printInt(parseInt(arg));
        } else if (name.equals("printFloat")) {
            if (!isFloatLiteral(arg)) {
                arg = floatMap.get(arg);
            }
            printFloat(parseFloat(arg));
        } else if (name.equals("printBool")) {
            if(!isBoolLiteral(arg)) {
                arg = boolMap.get(arg);
            }
            printBool(Boolean.parseBoolean(arg));
        } else if (name.equals("println")) {
            println();
        }
        return "";
    }

    private String evaluate(String lhs, String op, String rhs) {
        int type = 0;

        // sets values for literals
        if (isIntLiteral(lhs) && isIntLiteral(rhs)) {
            type = 1;
        } if (isFloatLiteral(lhs) && isFloatLiteral(rhs)) {
            type = 2;
        } else {
            type = 3;
        }

        // simplifies values if possible
        if (intMap.containsKey(lhs)) {
            lhs = intMap.get(lhs);
            type = 1;
        }
        if (intMap.containsKey(rhs)) {
            rhs = intMap.get(rhs);
            type = 1;
        }
        if (floatMap.containsKey(lhs)) {
            lhs = floatMap.get(lhs);
            type = 2;
        }
        if (floatMap.containsKey(rhs)) {
            rhs = floatMap.get(rhs);
            type = 2;
        }
        if (floatMap.containsKey(lhs)) {
            lhs = boolMap.get(lhs);
            type = 3;
        }
        if (floatMap.containsKey(rhs)) {
            rhs = boolMap.get(rhs);
            type = 3;
        }

        // perform operations
        if (op.equals("+")) {
            if (type == 1) {
                int newVal = parseInt(lhs) + parseInt(rhs);
                return Integer.toString(newVal);
            }
            if (type == 2) {
                float newVal = parseFloat(lhs) + parseFloat(rhs);
                return Float.toString(newVal);
            }
        } else if (op.equals("-")) {
            if (type == 1) {
                int newVal = parseInt(lhs) - parseInt(rhs);
                return Integer.toString(newVal);
            }
            if (type == 2) {
                float newVal = parseFloat(lhs) - parseFloat(rhs);
                return Float.toString(newVal);
            }
        } else if (op.equals("*")) {
            if (type == 1) {
                int newVal = parseInt(lhs) * parseInt(rhs);
                return Integer.toString(newVal);
            }
            if (type == 2) {
                float newVal = parseFloat(lhs) * parseFloat(rhs);
                return Float.toString(newVal);
            }
        } else if (op.equals("/")) {
            if (type == 1) {
                int newVal = parseInt(lhs) / parseInt(rhs);
                return Integer.toString(newVal);
            }
            if (type == 2) {
                float newVal = parseFloat(lhs) / parseFloat(rhs);
                return Float.toString(newVal);
            }
        } else if (op.equals("%")) {
            if (type == 1) {
                int newVal = parseInt(lhs) % parseInt(rhs);
                return Integer.toString(newVal);
            }
            if (type == 2) {
                float newVal = parseFloat(lhs) % parseFloat(rhs);
                return Float.toString(newVal);
            }
        } else if (op.equals("^")) {
            if (type == 1) {
                int newVal = (int) Math.pow(parseInt(lhs), parseInt(rhs));
                return Integer.toString(newVal);
            }
//            if (type == 2) {
//                float newVal = (int) Math.pow(parseFloat(lhs) % parseFloat(rhs);
//                return Float.toString(newVal);
//            }
        } else if (op.equals("and") && type == 3) {
            boolean newVal = Boolean.parseBoolean(lhs) && Boolean.parseBoolean(rhs);
            return Boolean.toString(newVal);
        } else if (op.equals("or") && type == 3) {
            boolean newVal = Boolean.parseBoolean(lhs) || Boolean.parseBoolean(rhs);
            return Boolean.toString(newVal);
        } else if (op.equals("==")) {
            return Boolean.toString(lhs.equals(rhs));
        } else if (op.equals("!=")) {
            return Boolean.toString(!lhs.equals(rhs));
        } else if (op.equals("<")) {
            if (type == 1) {
                boolean newVal = parseInt(lhs) < parseInt(rhs);
                return Boolean.toString(newVal);
            } else if (type == 2) {
                boolean newVal = parseFloat(lhs) < parseFloat(rhs);
                return Boolean.toString(newVal);
            }
        } else if (op.equals(">")) {
            if (type == 1) {
                boolean newVal = parseInt(lhs) > parseInt(rhs);
                return Boolean.toString(newVal);
            } else if (type == 2) {
                boolean newVal = parseFloat(lhs) > parseFloat(rhs);
                return Boolean.toString(newVal);
            }
        } else if (op.equals("<=")) {
            if (type == 1) {
                boolean newVal = parseInt(lhs) <= parseInt(rhs);
                return Boolean.toString(newVal);
            } else if (type == 2) {
                boolean newVal = parseFloat(lhs) <= parseFloat(rhs);
                return Boolean.toString(newVal);
            }
        } else if (op.equals(">=")) {
            if (type == 1) {
                boolean newVal = parseInt(lhs) >= parseInt(rhs);
                return Boolean.toString(newVal);
            } else if (type == 2) {
                boolean newVal = parseFloat(lhs) >= parseFloat(rhs);
                return Boolean.toString(newVal);
            }
        }

        return lhs;
    }

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
        return parseInt(nextInput());
    }

    private float readFloat () {
        System.out.print("float? ");
        return parseFloat(nextInput());
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

    // literal = integerLit | floatLit
    private Token literal () {
        return matchNonTerminal(NonTerminal.LITERAL);
    }

    // designator = ident { "[" relExpr "]" }
    private String designator () {
        int lineNum = lineNumber();
        int charPos = charPosition();

        Token ident = expectRetrieve(Token.Kind.IDENT);
        String expr = ident.lexeme();
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expr+="[";
            expr += relExpr();
            accept(Token.Kind.CLOSE_BRACKET);
            expr+="]";
        }
        return expr;
    }

    // computation	= "main" {varDecl} {funcDecl} "{" statSeq "}" "."
    private void computation () {

        expect(Token.Kind.MAIN);

        // deal with varDecl 0 or many
        while (have(NonTerminal.VAR_DECL)) {
            varDecl();
        }

        // deal with funcDecl 0 or many
//        while (have(NonTerminal.FUNC_DECL)) {
//            funcDecl();
//        }

        expect(Token.Kind.OPEN_BRACE);
        statSeq();
        expect(Token.Kind.CLOSE_BRACE);
        expect(Token.Kind.PERIOD);
    }

    // varDecl = typeDecl ident {"," ident} ";"
    private void varDecl() {
        int type = typeDecl();
        do {
            String ident = expectRetrieve(Token.Kind.IDENT).lexeme();
            insertMap(type, ident, "");
        } while(accept(Token.Kind.COMMA));
        expect(Token.Kind.SEMICOLON);
    }

    // typeDecl = type  { "[" integerLit "]" }
    private int typeDecl() {
        int type = 0;
        if (accept(Token.Kind.INT)) {
            type = 1;
        } else if (accept(Token.Kind.FLOAT)) {
            type = 2;
        } else if (accept(Token.Kind.BOOL)) {
            type = 3;
        } else {
            expect(NonTerminal.TYPE_DECL);
        }
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expect(Token.Kind.INT_VAL);
            expect(Token.Kind.CLOSE_BRACKET);
        }

        return type;

    }

    // funcDecl = "function" ident formalParam ":" ("void" | type ) funcBody
//    private void funcDecl() {
//        expect(Token.Kind.FUNC);
//        expect(Token.Kind.IDENT);
//        formalParam();
//        expect(Token.Kind.COLON);
//
//        // searches for either a VOID or a TYPE
//        if (!(accept(Token.Kind.VOID) || accept(NonTerminal.TYPE_DECL))) {
//            // may need to change this to reflect something other than expecting VOID
//            expect(NonTerminal.FUNC_DECL);
//        }
//
//        funcBody();
//    }

    // formalParam = "(" [paramDecl { "," paramDecl}] ")"
//    private void formalParam() {
//        expect(Token.Kind.OPEN_PAREN);
//        if (have(NonTerminal.PARAM_DECL)){
//            do {
//                paramDecl();
//            } while(accept(Token.Kind.COMMA));
//        }
//        expect(Token.Kind.CLOSE_PAREN);
//
//    }

    // paramDecl = paramType ident
//    private void paramDecl() {
//        paramType();
//        expect(Token.Kind.IDENT);
//    }

    // paramType = type {"[" "]"}
//    private void paramType() {
//        expect(NonTerminal.TYPE_DECL);
//        while (accept(Token.Kind.OPEN_BRACKET)) {
//            expect(Token.Kind.CLOSE_BRACKET);
//        }
//    }
//
//    // funcBody = "{" {varDecl}  statSeq "}" ";"
//    private void funcBody() {
//        expect(Token.Kind.OPEN_BRACE);
//        while (have(NonTerminal.VAR_DECL)) {
//            varDecl();
//        }
//
//        statSeq();
//
//        expect(Token.Kind.CLOSE_BRACE);
//        expect(Token.Kind.SEMICOLON);
//    }

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
        String name = designator();
        String operator = "";
        if (have(NonTerminal.ASSIGN_OP)) {
            operator += assignOp();
            String expr = relExpr();
            assignment(name, operator, expr);
        } else if (have(NonTerminal.UNARY_OP)) {
            operator += unaryOp();
            assignmentUnary(name, operator);
        } else {
            expect(NonTerminal.ASSIGN);
        }
    }

    private String assignOp() {
        String operator = "";
        if (accept(Token.Kind.ASSIGN)) {
            operator = "=";
        } else if (accept(Token.Kind.ADD_ASSIGN)) {
            operator = "+=";
        } else if (accept(Token.Kind.SUB_ASSIGN)) {
            operator = "-=";
        } else if (accept(Token.Kind.DIV_ASSIGN)) {
            operator = "/=";
        } else if (accept(Token.Kind.MUL_ASSIGN)) {
            operator = "*=";
        } else if (accept(Token.Kind.MOD_ASSIGN)) {
            operator = "%=";
        } else if (accept(Token.Kind.POW_ASSIGN)) {
            operator = "^=";
        }
        return operator;
    }

    private String unaryOp() {
        if (accept(Token.Kind.UNI_INC)) {
            return "++";
        } else if (accept(Token.Kind.UNI_DEC)) {
            return "--";
        }
        return "";
    }




    // relExpr = addExpr { relOp addExpr }
    private String relExpr () {
        // for empty return statements
        String leftSide = "";
        if(have(Token.Kind.SEMICOLON) || have(Token.Kind.CLOSE_PAREN)){
            return leftSide;
        }
        leftSide += addExpr();
        while (have(NonTerminal.REL_OP)){
            String operator = expectRetrieve(NonTerminal.REL_OP).lexeme();
            String rightSide = addExpr();
            leftSide = evaluate(leftSide, operator, rightSide);
        }
        return leftSide;
    }

    // addExpr = multExpr { addOp multExpr }
    private String addExpr () {
        String leftSide = multExpr();
        while (accept(Token.Kind.ADD) || accept(Token.Kind.SUB) || accept(Token.Kind.OR)) {
            String operator = "";
            if (accept(Token.Kind.ADD)) {
                operator = "+";
            } else if (accept(Token.Kind.SUB)) {
                operator = "-";
            } else if (accept(Token.Kind.OR)) {
                operator = "or";
            }
            String rightSide = multExpr();
            leftSide = evaluate(leftSide, operator, rightSide);
        }
        return leftSide;
    }

    // multExpr = powExpr { multOp powExpr }
    private String multExpr() {

        String leftSide = powExpr();

        while (have(Token.Kind.MUL) || have(Token.Kind.DIV) || have(Token.Kind.MOD) || have(Token.Kind.AND)) {
            String operator ="";
            if (accept(Token.Kind.MUL)) {
                operator = "*";
            } else if (accept(Token.Kind.DIV)) {
                operator = "/";
            } else if (accept(Token.Kind.MOD)) {
                operator = "%";
            } else if (accept(Token.Kind.AND)) {
                operator = "and";
            }
            String rightSide = powExpr();
            leftSide = evaluate(leftSide, operator, rightSide);
        }
        return leftSide;
    }

    // powExpr = groupExpr { powOp groupExpr }
    private String powExpr () {
        String leftSide = groupExpr();
        while (accept(Token.Kind.POW)) {
            String rightSide = groupExpr();
            leftSide = evaluate(leftSide, "^", rightSide);
        }
        return leftSide;
    }

    // groupExpr = literal | designator | "not" relExpr | relation | funcCall
    private String groupExpr() {
        if (have(NonTerminal.LITERAL)) {
            //System.out.println("lit" + lineNumber());
            if (have(Token.Kind.INT_VAL)) {
                return expectRetrieve(Token.Kind.INT_VAL).lexeme();
            } else if (have(Token.Kind.FLOAT_VAL)) {
                return expectRetrieve((Token.Kind.FLOAT)).lexeme();
            } else if (have(NonTerminal.BOOL_LIT)) {
                return expectRetrieve(NonTerminal.BOOL_LIT).lexeme();
            }
        } else if (have(NonTerminal.DESIGNATOR)) {
           // System.out.println("des" + lineNumber());
            return designator();
        } else if (accept(Token.Kind.NOT)) {
           // System.out.println("relExp" + lineNumber());
            String expr =  relExpr();

            // if not a literal value get the literal value corresponding
            if (!expr.equals("true") && !expr.equals("false")) {
                expr = boolMap.get(expr);
            }

            // negate expr
            if (expr.equals("true")) {
                return "false";
            } else if (expr.equals("false")) {
                return "true";
            }
        } else if (have(Token.Kind.OPEN_PAREN)) {
            //System.out.println("realtion" + lineNumber());
            return relation();
        } else if (have(NonTerminal.FUNC_CALL)){
            //System.out.println("call" + lineNumber());
            return funcCall();
        } else {
            expect(NonTerminal.GROUP_EXPR);
        }
        return "";
    }

    // relation = "(" relExpr ")"
    private String relation() {
        expect(Token.Kind.OPEN_PAREN);
        String expr = relExpr();
        expect(Token.Kind.CLOSE_PAREN);
        return expr;
    }

    // funCall = "call" ident "(" [ relExpr { "," relExpr } ] ")"
    private String funcCall() {

        expect(Token.Kind.CALL);
        String functionName = expectRetrieve(Token.Kind.IDENT).lexeme();
        expect(Token.Kind.OPEN_PAREN);
        String arg = relExpr();
        // only have function with single arguments
        while (accept(Token.Kind.COMMA)) {
            relExpr();
        }
        expect(Token.Kind.CLOSE_PAREN);

        return runFunc(functionName, arg);
    }

    // ifStat = "if" relation "then" statSeq [ "else" statSeq ] "fi"
    private void ifStat() {
        expect(Token.Kind.IF);
        String relation = relation();

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
