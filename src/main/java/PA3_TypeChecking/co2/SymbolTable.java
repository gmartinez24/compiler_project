package co2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import types.*;

public class SymbolTable {

    // TODO: Create Symbol Table structure
    // Make a Hashtable with the key being the variable name and the value being the type
    // question is about scope
    List<HashMap<String, Symbol>> Scopes;

    int currentScope;
    // create a stack of scopes
    // each scope is a hashtable as described above
    public SymbolTable () {

        Scopes = new ArrayList<>();
        HashMap<String, Symbol> globalScope = new HashMap<>();
        Scopes.add(globalScope);
        // put predefined functions in global scope

        // int readInt()
        TypeList readIntType = new TypeList();
        readIntType.append(new IntType());
        Symbol readInt = new Symbol(readIntType, "readInt", 0, 0);
        globalScope.put("readInt", readInt);

        // float readFloat()
        TypeList readFloatType = new TypeList();
        readIntType.append(new FloatType());
        Symbol readFloat = new Symbol(readFloatType, "readFloat", 0, 0);
        globalScope.put("readFloat", readFloat);


        // bool readBool()
        TypeList readBoolType = new TypeList();
        readBoolType.append(new BoolType());
        Symbol readBool = new Symbol(readBoolType, "readBool", 0, 0);
        globalScope.put("readBool", readBool);

        // void printInt(int arg)
        TypeList printIntType = new TypeList();
        printIntType.append(new IntType());
        printIntType.append(new VoidType());
        Symbol printInt = new Symbol(printIntType, "printInt", 0, 0);
        globalScope.put("printInt", printInt);

        // void printFloat(int arg)
        TypeList printFloatType = new TypeList();
        printFloatType.append(new IntType());
        printFloatType.append(new VoidType());
        Symbol printFloat = new Symbol(printFloatType, "printFloat", 0, 0);
        globalScope.put("printFloat", printFloat);

        // void printBool(bool arg)
        TypeList printBoolType = new TypeList();
        printBoolType.append(new IntType());
        printBoolType.append(new VoidType());
        Symbol printBool = new Symbol(printBoolType, "printBool", 0, 0);
        globalScope.put("printBool", printBool);

        // void println()
        TypeList printlnType = new TypeList();
        printlnType.append(new VoidType());
        Symbol println = new Symbol(printlnType, "println", 0, 0);
        globalScope.put("println", println);

        // void arrcpy(T[] dest, T[] src, int n)
        TypeList arrcpyType = new TypeList();
        arrcpyType.append(new ArrayType());
        arrcpyType.append(new ArrayType());
        arrcpyType.append(new IntType());
        arrcpyType.append(new VoidType());
        Symbol arrcpy = new Symbol(arrcpyType, "arrcpy", 0, 0 );
        globalScope.put("arrcpy", arrcpy);

        currentScope = 0;
        //throw new RuntimeException("Create Symbol Table and initialize predefined functions");
    }

    // lookup name in SymbolTable
    public Symbol lookup (String name) throws SymbolNotFoundError {
        Symbol sym = Scopes.get(currentScope).get(name);
        System.out.println(Scopes.get(currentScope).toString());
        int scope = currentScope;
        System.out.println(scope);
        while (scope > 0 && sym == null) {
            scope--;
            System.out.println(Scopes.get(currentScope).toString());

            sym = Scopes.get(scope).get(name);
        }

        if (sym == null) {
            throw new SymbolNotFoundError(name);
        } else {
            return sym;
        }
        //throw new RuntimeException("implement lookup variable");
    }

    // insert name in SymbolTable
    public Symbol insert (Symbol sym) throws RedeclarationError {
        //If the lookup throws an error, doesn't exist so we can insert
        //Changing this algo
        try{
            this.lookup(sym.name());
            throw new RedeclarationError(sym.name());
        }
        catch(SymbolNotFoundError e){
            return Scopes.get(currentScope).put(sym.name(), sym);
        }

        //throw new RuntimeException("implement insert variable");
    }

    public void enterScope () {
        currentScope++;
        Scopes.add(new HashMap<String, Symbol>());
    }

    public void exitScope () {
        if (currentScope == 0) {
            throw new RuntimeException("Cannot remove global scope.");
        }
        Scopes.remove(currentScope);
        currentScope--;
    }
}

class SymbolNotFoundError extends Error {

    private static final long serialVersionUID = 1L;
    private final String name;

    public SymbolNotFoundError (String name) {
        super("Symbol " + name + " not found.");
        this.name = name;
    }

    public String name () {
        return name;
    }
}

class RedeclarationError extends Error {

    private static final long serialVersionUID = 1L;
    private final String name;

    public RedeclarationError (String name) {
        super("Symbol " + name + " being redeclared.");
        this.name = name;
    }

    public String name () {
        return name;
    }
}
