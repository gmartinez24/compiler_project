package co2;
import ast.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import types.*;

import javax.imageio.stream.FileImageInputStream;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.reverse;

public class Interpreter {
    AST ast;

    BufferedReader in;

    List<String> values = new ArrayList<>();

    int argsRead = 0;

    HashMap<String, String> intMap = new HashMap<>();
    HashMap<String, String> floatMap = new HashMap<>();
    HashMap<String, String> boolMap = new HashMap<>();

    public Interpreter(AST ast, InputStream in) {
        this.ast = ast;
        this.in  = new BufferedReader(new InputStreamReader(in));

        String line;

        try {
            while((line = this.in.readLine()) != null) {
                String[] temp = line.split(" ");
                values.addAll(Arrays.asList(temp));
            }
        } catch (IOException e) {
            System.out.println("Error with input in interpreter");
        }

    }

    public void interpret() {
        computation();
    }

    private String nextInput () {
//        while (st == null || !st.hasMoreElements()) {
//                st = new StringTokenizer(reader.readLine());
        if (argsRead < values.size()) {
            String retStr =  values.get(argsRead);
            argsRead++;
            return retStr;

        } else {
            throw new RuntimeException("Not enough args");
        }
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
           // Boolean.parseBoolean(expr);
            return expr.equals("true") || expr.equals("false");
        } catch (Exception e) {
            return false;
        }
    }

    private void assignment(String name, String op, String expr) {
        String currValue = "";
        String newVal = "";

        int type = 0;

        // search maps for given name
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

        if (op.equals("=")) {
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
            // tree is set up to handle += and ++ the same
        } else if (op.equals("+=") || op.equals("++")) {
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
            // tree is set up to handle -= and -- the same
        } else if (op.equals("-=") || op.equals("--")) {
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
        } else if (op.equals("*=")) {
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

        } else if (op.equals("/=")) {
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

        } else if (op.equals("%=")) {
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

        } else if (op.equals("^=")) {
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
        if (boolMap.containsKey(lhs)) {
            lhs = boolMap.get(lhs);
            type = 3;
        }
        if (boolMap.containsKey(rhs)) {
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
            if (type == 2) {
                float newVal = (int) Math.pow(parseFloat(lhs), parseFloat(rhs));
                return Float.toString(newVal);
            }
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

    private String runFunction(String name, String arg) {
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

    private void computation() {
        Computation root = ast.root();
        DeclarationList vars = root.variables();
        if (vars != null){
            for (Declaration var : vars.declarationList()) {
                Symbol varSym = var.symbol();
                Type varType = varSym.type();
                int type = 0;
                if (varType instanceof IntType) {
                    type = 1;
                } else if (varType instanceof FloatType) {
                    type = 2;
                } else if (varType instanceof BoolType) {
                    type = 3;
                }
                insertMap(type, varSym.name(), "");
            }
        }

        // dont care about function declarations

        statSeq(root.mainStatementSequence());
    }

    public void statSeq(StatementSequence statementSequence){
        ArrayList<Statement> statements = statementSequence.getStatements();
        for (Statement statement : statements) {

            if (statement instanceof ast.IfStatement) {
                ifStatement((IfStatement)statement);
            } else if (statement instanceof ReturnStatement) {
                returnStatement((ReturnStatement) statement);
            } else if (statement instanceof Assignment) {
                assign((Assignment) statement);
            } else if (statement instanceof FunctionCall) {
                functionCall((FunctionCall)statement);
            }
        }
    }

    public void assign(Assignment assign) {
        String name = designator(assign.lhs());
        String expr = relExpr(assign.rhs());
        String operator = assign.operator();
        if (operator == null) {
            operator = "=";
        }
        assignment(name, operator, expr);
    }

    public String designator(Expression expr) {
        Identifier ident = (Identifier) expr;
        String desig = ident.symbol().name();
        Symbol identSym = ident.symbol();
        List<Expression> indexList = ident.indexList();
        if (indexList != null) {
            for (Expression index : indexList) {
                desig+="[";
                desig+=relExpr(index);
                desig+="]";
            }
            if (!(intMap.containsKey(desig) || floatMap.containsKey(desig) || boolMap.containsKey(desig))) {
                if (((ArrayType)identSym.type()).elementType() instanceof IntType) {
                    intMap.put(desig, "");
                } else if (((ArrayType)identSym.type()).elementType() instanceof FloatType) {
                    floatMap.put(desig, "");
                } else if (((ArrayType)identSym.type()).elementType() instanceof BoolType){
                    boolMap.put(desig, "");
                }
            }

        }

        return desig;

        // TODO: Handle arrays
    }

    // TODO: figure out if this is working
    public String relExpr(Expression expr) {
        String leftSide = "";
        if (expr.lhs() == null) {
            leftSide += addExpr(expr);
        }
        else if (!(expr instanceof Relation)){
          //leftSide += addExpr(expr.lhs());
            leftSide += addExpr(expr);
        }


        if (expr instanceof Relation) {
            Expression temp = expr;
            leftSide += addExpr(expr.lhs());
            String tempRight = addExpr(temp.rhs());
            String tempOperator = ((Relation)temp).operator();
            leftSide = evaluate(leftSide, tempOperator, tempRight);
            temp = temp.rhs();
            while (temp.rhs() instanceof Relation) {
                String rightSide = addExpr(temp.rhs());
                String operator = ((Relation) temp).operator();
                leftSide = evaluate(leftSide, operator, rightSide);
                temp = temp.rhs();
            }

        }

        return leftSide;
    }

    public String addExpr(Expression expr) {
        String leftSide = "";
        if (expr.lhs() == null) {
            leftSide += mulExpr(expr);
        }
        else if (!(expr instanceof Addition || expr instanceof Subtraction || expr instanceof LogicalOr)){
            //leftSide += mulExpr(expr.lhs());
            leftSide += mulExpr(expr);
        }

        if (expr instanceof Addition || expr instanceof Subtraction || expr instanceof LogicalOr) {
            Expression temp = expr;
            do {
                leftSide += mulExpr(expr.lhs());
                String rightSide = mulExpr(temp.rhs());
                String operator = "";
                if (temp instanceof Addition) {
                    operator = "+";
                } else if (temp instanceof Subtraction) {
                    operator = "-";
                } else {
                    operator = "or";
                }
                leftSide = evaluate(leftSide, operator, rightSide);
                temp = temp.rhs();
            } while (temp.rhs() instanceof Addition || temp.rhs() instanceof Subtraction || temp.rhs() instanceof LogicalOr);
        }
        return leftSide;
    }

    public String mulExpr(Expression expr) {
        String leftSide = "";
        if (expr.lhs() == null) {
            leftSide += powExpr(expr);
        }
        else if (!(expr instanceof Multiplication || expr instanceof Division || expr instanceof Modulo || expr instanceof LogicalAnd)){
            //leftSide += powExpr(expr.lhs());
            leftSide = powExpr(expr);
        }

        if (expr instanceof Multiplication || expr instanceof Division || expr instanceof Modulo || expr instanceof LogicalAnd) {
            Expression temp = expr;
            do {
                leftSide += powExpr(expr.lhs());
                String rightSide = powExpr(temp.rhs());
                String operator;
                if (temp instanceof Multiplication) {
                    operator = "*";
                } else if (temp instanceof Division) {
                    operator ="/";
                } else if (temp instanceof  Modulo) {
                    operator = "%";
                } else {
                    operator = "and";
                }
                leftSide = evaluate(leftSide, operator, rightSide);
               temp = temp.rhs();
           } while (temp.rhs() instanceof Multiplication|| temp.rhs() instanceof Division || temp.rhs() instanceof Modulo || temp.rhs() instanceof LogicalAnd);
        }
        return leftSide;
    }

    public String powExpr(Expression expr) {
        String leftSide = "";
        if (expr.lhs() == null) {
            leftSide += groupExpr(expr);
        }
        else if (!(expr instanceof Power)){
           // leftSide += groupExpr(expr.lhs());
            leftSide = groupExpr(expr);
        }
        if (expr instanceof Power) {
            Expression temp = expr;
            do {
                leftSide += groupExpr(expr.lhs());
                String rightSide = groupExpr(temp.rhs());
                String operator = "^";
                leftSide = evaluate(leftSide, operator, rightSide);
                temp = temp.rhs();
            } while (temp.rhs() instanceof Power);
        }
        return leftSide;
    }



    public String groupExpr(Expression expr) {
        if (expr instanceof BoolLiteral) {
            return Boolean.toString(((BoolLiteral)expr).value());
        } else if (expr instanceof IntegerLiteral) {
            return Integer.toString(((IntegerLiteral)expr).value());
        } else if (expr instanceof FloatLiteral) {
            return Float.toString(((FloatLiteral)expr).value());
        } else if (expr instanceof Identifier) {
            return designator(expr);
        } else if (expr instanceof LogicalNot) {
            LogicalNot not = (LogicalNot) expr;
            String toNegate = relExpr(not.expression());
            if (!toNegate.equals("false") && !toNegate.equals("true")) {
                toNegate = boolMap.get(toNegate);
            }

            if(toNegate.equals("true")) {
                return "false";
            } else if (toNegate.equals("false")) {
                return "true";
            }
        } else if (expr instanceof Relation) {
            return relExpr(expr);
        } else if (expr instanceof FunctionCall) {
            return functionCall((FunctionCall) expr);
        }
        return relExpr(expr);
    }

    private String functionCall(FunctionCall functionCall) {
        Symbol funcSym = functionCall.symbol();
        // get first and only argument;
        String arg = "";
        if (!functionCall.args().args().isEmpty()) {
            Expression argExp = functionCall.args().args().get(0);


           arg = relExpr(argExp);
        }

        return runFunction(funcSym.name(), arg);
    }

    private void ifStatement(IfStatement ifStatement) {
        String val = relExpr(ifStatement.relation());
        if (val.equals("true")) {
            statSeq(ifStatement.ifBlock());
        } else {
            statSeq(ifStatement.elseBlock());
        }

    }

    private void returnStatement(ReturnStatement returnStatement) {
        // checking for empty return statement
        if (returnStatement.relation().lhs() != null) {
            System.exit(0);
            return;
        }
    }


}
