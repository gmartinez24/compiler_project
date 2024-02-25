package co2;

public class Token {

    public enum Kind {
        // boolean operators
        AND("and"),
        OR("or"),
        NOT("not"),

        // arithmetic operators
        POW("^"),

        MUL("*"),
        DIV("/"),
        MOD("%"),

        ADD("+"),
        SUB("-"),

        // relational operators
        EQUAL_TO("=="),
        NOT_EQUAL("!="),
        LESS_THAN("<"),
        LESS_EQUAL("<="),
        GREATER_EQUAL(">="),
        GREATER_THAN(">"),

        // assignment operators
        ASSIGN("="),
        ADD_ASSIGN("+="),
        SUB_ASSIGN("-="),
        MUL_ASSIGN("*="),
        DIV_ASSIGN("/="),
        MOD_ASSIGN("%="),
        POW_ASSIGN("^="),

        // unary increment/decrement
        UNI_INC("++"),
        UNI_DEC("--"),

        // primitive types
        VOID("void"),
        BOOL("bool"),
        INT("int"),
        FLOAT("float"),

        // boolean literals
        TRUE("true"),
        FALSE("false"),

        // region delimiters
        OPEN_PAREN("("),
        CLOSE_PAREN(")"),
        OPEN_BRACE("{"),
        CLOSE_BRACE("}"),
        OPEN_BRACKET("["),
        CLOSE_BRACKET("]"),

        // field/record delimiters
        COMMA(","),
        COLON(":"),
        SEMICOLON(";"),
        PERIOD("."),

        // control flow statements
        IF("if"),
        THEN("then"),
        ELSE("else"),
        FI("fi"),

        WHILE("while"),
        DO("do"),
        OD("od"),

        REPEAT("repeat"),
        UNTIL("until"),

        CALL("call"),
        RETURN("return"),

        // keywords
        MAIN("main"),
        FUNC("function"),

        // special cases
        INT_VAL(),
        FLOAT_VAL(),
        IDENT(),

        EOF(),

        ERROR();

        private String defaultLexeme;

        Kind () {
            defaultLexeme = "";
        }

        Kind (String lexeme) {
            defaultLexeme = lexeme;
        }

        public boolean hasStaticLexeme () {
            return defaultLexeme != null;
        }

        // OPTIONAL: convenience function - boolean matches (String lexeme)
        //           to report whether a Token.Kind has the given lexeme
        //           may be useful

        public boolean matches (String lexeme) {
            for (Kind kind : Kind.values()) {
                if (kind.hasStaticLexeme() && kind.defaultLexeme.equals(lexeme)) {
                    return true;
                }
            }
            return false;
        }
    }

    private int lineNum;
    private int charPos;
    Kind kind;  // package-private
    private String lexeme = "";


    // implement remaining factory functions for handling special cases (EOF below)
    public static Token INT_VALUE ( String lexeme, int lineNum, int charPos) {
        Token tok = new Token(lexeme, lineNum, charPos);
        tok.kind = Kind.INT_VAL;
        tok.lexeme = lexeme;
        return tok;
    }
    public static Token FLOAT_VALUE (String lexeme, int lineNum, int charPos) {
        Token tok = new Token(lexeme, lineNum, charPos);
        tok.kind = Kind.FLOAT_VAL;
        tok.lexeme = lexeme;
        return tok;
    }
    public static Token IDENT (String lexeme, int lineNum, int charPos) {
        Token tok = new Token(lexeme, lineNum, charPos);
        tok.kind = Kind.IDENT;
        tok.lexeme = lexeme;
        return tok;
    }
    public static Token ERROR (int lineNum, int charPos) {
        // dont need lexeme for ERROR, kind and lexeme set in 2 parameter constructor
        return new Token(lineNum, charPos);
//        tok.kind = Kind.ERROR;
//        tok.lexeme = lexeme;
//        return tok;
    }
    public static Token EOF (int lineNum, int charPos) {
        Token tok = new Token(lineNum, charPos);
        tok.kind = Kind.EOF;
        return tok;
    }

    private Token (int lineNum, int charPos) {
        this.lineNum = lineNum;
        this.charPos = charPos;

        // no lexeme provide, signal error
        this.kind = Kind.ERROR;
        this.lexeme = "No Lexeme Given";
    }

    public Token (String lexeme, int lineNum, int charPos) {
        this.lineNum = lineNum;
        this.charPos = charPos;

//         based on the given lexeme determine and set the actual kind
//         if we don't match anything, signal error
        if(!determineTokenKind(lexeme)) {
            this.kind = Kind.ERROR;
            this.lexeme = "Unrecognized lexeme: " + lexeme;
        } else {
            this.lexeme = lexeme;
        }
    }

    public int lineNumber () {
        return lineNum;
    }

    public int charPosition () {
        return charPos;
    }

    public String lexeme () {
        // TODO: implement
        return lexeme;
    }

    public Kind kind () {
        // TODO: implement
        return kind;
    }

    // function to query a token about its kind - boolean is (Token.Kind kind)
    public boolean is(Token.Kind kind) {
        return kind.equals(this.kind);
    }

    // OPTIONAL: add any additional helper or convenience methods
    //           that you find make for a cleaner design

    // determines the kind of the Token based on Kind enum, if match found returns true, not found returns false
    public boolean determineTokenKind (String lexeme) {
        // loops through enum Kind and searches for match of given lexeme and static lexeme
        for (Kind kind : Kind.values()) {
            if (kind.hasStaticLexeme() && kind.defaultLexeme.equals(lexeme)) {
                this.kind = kind;
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString () {
        return "Line: " + lineNum + ", Char: " + charPos + ", Lexeme: " + lexeme;
    }
}
