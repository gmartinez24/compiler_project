package co2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Scanner implements Iterator<Token> {

    private BufferedReader input;   // buffered reader to read file
    private boolean closed; // flag for whether reader is closed or not

    private int lineNum;    // current line number
    private int charPos;    // character offset on current line

    private String scan;    // current lexeme being scanned in
    private int nextChar;   // contains the next char (-1 == EOF)

    private boolean error;  // true if error is detected

    // reader will be a FileReader over the source file
    public Scanner (Reader reader) {
        input = new BufferedReader(reader);
        closed = false;
        lineNum = 1;
        charPos = 0;
        scan = "";
        nextChar = ' ';
        error = false;

    }

    // signal an error message
    public void Error (String msg, Exception e) {
        System.err.println("Scanner: Line - " + lineNum + ", Char - " + charPos);
        if (e != null) {
            e.printStackTrace();
        }
        System.err.println(msg);
    }

    /*
     * helper function for reading a single char from input
     * can be used to catch and handle any IOExceptions,
     * advance the charPos or lineNum, etc.
     */
    private int readChar (){
        // TODO: fix line num and character positioning
        // adjusting line num and char pos in here may lead to incorrect positioning

        try {
            input.mark(1);
            char c = (char)input.read();
            charPos++;
            if (c == '\n') {
                lineNum++;
                charPos = 0;
            }

            // ignore inline comments
            if (c == '/' && nextChar == '/') {
                input.readLine();
                c = (char)input.read();
                lineNum++;
                charPos = 0;
            }

            // ignore block comments
            if (c == '*' && nextChar == '/') {
                c = (char) skipBlockComment();
            }
            return c;
        } catch (IOException e) {
            System.err.println("Error reading character at Line:" + lineNum + "Pos: " + charPos);
        }
        return -1;
    }

    /*
     * function to query whether more characters can be read
     * depends on closed and nextChar
     */
    @Override
    public boolean hasNext () {
        // TODO: implement
//        System.out.println("Next Char: " + (char)nextChar + " Closed: " + closed);
//        System.out.println("LineNum: " + lineNum + " Char Pos: " + charPos);
//        return nextChar != 65535 && !closed;
        return !closed;
    }

    /*
     *	returns next Token from input
     *
     *  invariants:
     *  1. call assumes that nextChar is already holding an unread character
     *  2. return leaves nextChar containing an untokenized character
     *  3. closes reader when emitting EOF
     */
    // TODO: Modify to move sections of code into functions
    // TODO: Add in ERROR recogniition (opposite of maximal munch implementation at bottom)
    @Override
    public Token next () {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (nextChar == -1) {
            try {
                closed = true;
                input.close();
                scan = "";
                return Token.EOF(lineNum, charPos);
            } catch (IOException e) {
                System.err.println("Error closing the file");
            }
        }
        // disregard whitespace (Dragon Fig 2.29)
        for(;;) {
            nextChar = (char)readChar();
            if (nextChar == ' ' || nextChar == '\t') continue;
            else if (nextChar == '\n') continue;
            else if (nextChar == '/') {
                try {
                    input.mark(1);
                    int c = input.read();
                    input.reset();
                    if (c == '*' || c == '/') {
                        continue;
                    } else {
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("Error with /");
                }
                //continue;
            }
            else break;
        }

        if (error) {
            return Token.ERROR(lineNum,charPos);
        }

        // if nextChar is a digit (Modified fromDragon Fig 2.30)
        // TODO: handle floats and negatives
        if (nextChar == '-') {
            try {
                int c = readChar();
                if (Character.isDigit(c)) {
                    //input.reset();
                    scan += (char)nextChar;
                    nextChar = c;
                    return createNumberToken();
                } else {
                    input.reset();
                }
            } catch (IOException e) {
                System.out.println("Error reading from input");
            }

        }

        if (Character.isDigit(nextChar)) {
            return createNumberToken();
        }

        // if nextChar is a letter (Modified from Dragon 2.31)
        if (Character.isLetter(nextChar)) {
            do {
                scan += (char)nextChar;
                nextChar = readChar();
            } while (Character.isLetterOrDigit(nextChar));
            try {
                input.reset();
            } catch(IOException e) {
                System.out.println("Error resetting input");
            }

            String lexeme = scan;
            scan = "";
            Token tok = new Token(lexeme, lineNum, charPos);
            if (tok.is(Token.Kind.ERROR)) {
                return Token.IDENT(lexeme, lineNum, charPos);
            } else return tok;

        }
//        if (nextChar == -1) {
//            try {
//                closed = true;
//                input.close();
//                scan = "";
//                return Token.EOF(lineNum, charPos);
//            } catch (IOException e) {
//                System.err.println("Error closing the file");
//            }
//        }

        scan += (char)nextChar;

        String lexeme = "";
        Token tok = new Token (scan, lineNum, charPos);
        // special case for bang (!), if scan == ! then failure
        if (scan.equals("!")) {
            nextChar = readChar();
            scan+= (char)nextChar;
            tok = new Token(scan ,lineNum, charPos);
        }
        while (!tok.is(Token.Kind.ERROR)) {
            lexeme = scan;
            nextChar = readChar();
            scan += (char)nextChar;
            tok = new Token(scan ,lineNum, charPos);
        }
        // tok generated ERROR token on instantiation
        if (lexeme.equals("")) {
            scan = "";
            return Token.ERROR(lineNum, charPos);
        }
        scan = "";
        try {
            // resets the BufferedReader pointer back one (needed it to be put 1 ahead to read 1 char ahead)
            nextChar = lexeme.charAt(lexeme.length()-1);
            input.reset();
        } catch( IOException e) {
            System.err.println("Error resetting BufferedReader");
        }
        return new Token (lexeme, lineNum, charPos);
    }

    // OPTIONAL: add any additional helper or convenience methods
    //           that you find make for a cleaner design
    //           (useful for handling special case Tokens)

    /*
    * returns next character following the block comment
    *
    * Skips a block comment (assumes that the opening /* of a block comment has already been read|)
    */
    private int skipBlockComment() {
        try {
            int checkChar = input.read();
            charPos++;
            while (checkChar != '*') {
                checkChar = input.read();
                charPos++;
                if (checkChar == '\n') {
                    charPos = 0;
                    lineNum++;
                }
                if (checkChar == -1) {
                    error = true;
                    return -1;
                }
            }
            checkChar = input.read();
            if (checkChar == '/') {
                return input.read();
            } else {
                return skipBlockComment();
            }
        } catch (IOException e) {
            System.err.println("Error Skipping Block Comment");
        }
        return -1;
    }

    /* called with knowledge that next token should be a number
     *
     * Reads in the token and returns either an INT_VAL, FLOAT_VAL or ERROR
     *
    */
    private Token createNumberToken() {
        Integer v = 0;
        do {
            v = 10 * v + (Character.digit(nextChar, 10));
            nextChar = readChar();
        } while (Character.isDigit(nextChar));
        // for floating point values
        if (nextChar == '.') {
            scan += v.toString() + '.';
            v = 0;
            nextChar = readChar();
            if (!Character.isDigit(nextChar)) {
                try {
                    input.reset();
                } catch (IOException e) {
                    System.out.println("Error resetting input");
                }
                return Token.ERROR(lineNum, charPos);
            }
            do {
                v = 10 * v + (Character.digit(nextChar, 10));
                nextChar = readChar();
            } while (Character.isDigit(nextChar));
        }
        scan+=v.toString();
        try {
            input.reset();
        } catch(IOException e) {
            System.out.println("Error resetting input");
        }
        String lexeme = scan;
        scan="";
        // determine whether to return float or int
        if (lexeme.contains(".")) {
            return Token.FLOAT_VALUE(lexeme, lineNum, charPos);
        }
        return Token.INT_VALUE(lexeme, lineNum, charPos);
    }
}

