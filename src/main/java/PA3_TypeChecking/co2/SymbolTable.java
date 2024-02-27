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
        FuncType readIntType = new FuncType();
        readIntType.params().append(new IntType());
        Symbol readInt = new Symbol(readIntType, "readInt", 0, 0);
        globalScope.put("readInt", readInt);

        // float readFloat()
        FuncType readFloatType = new FuncType();
        readFloatType.params().append(new FloatType());
        Symbol readFloat = new Symbol(readFloatType, "readFloat", 0, 0);
        globalScope.put("readFloat", readFloat);


        // bool readBool()
        FuncType readBoolType = new FuncType();
        readBoolType.params().append(new BoolType());
        Symbol readBool = new Symbol(readBoolType, "readBool", 0, 0);
        globalScope.put("readBool", readBool);

        // void printInt(int arg)
        FuncType printIntType = new FuncType();
        printIntType.params().append(new IntType());
        printIntType.params().append(new VoidType());
        Symbol printInt = new Symbol(printIntType, "printInt", 0, 0);
        globalScope.put("printInt", printInt);

        // void printFloat(int arg)
        FuncType printFloatType = new FuncType();
        printFloatType.params().append(new IntType());
        printFloatType.params().append(new VoidType());
        Symbol printFloat = new Symbol(printFloatType, "printFloat", 0, 0);
        globalScope.put("printFloat", printFloat);

        // void printBool(bool arg)
        FuncType printBoolType = new FuncType();
        printBoolType.params().append(new IntType());
        printBoolType.params().append(new VoidType());
        Symbol printBool = new Symbol(printBoolType, "printBool", 0, 0);
        globalScope.put("printBool", printBool);

        // void println()
        FuncType printlnType = new FuncType();
        printlnType.params().append(new VoidType());
        Symbol println = new Symbol(printlnType, "println", 0, 0);
        globalScope.put("println", println);

        // void arrcpy(T[] dest, T[] src, int n)
        FuncType arrcpyType = new FuncType();
        arrcpyType.params().append(new ArrayType());
        arrcpyType.params().append(new ArrayType());
        arrcpyType.params().append(new IntType());
        arrcpyType.params().append(new VoidType());
        Symbol arrcpy = new Symbol(arrcpyType, "arrcpy", 0, 0 );
        globalScope.put("arrcpy", arrcpy);

        currentScope = 0;
        //throw new RuntimeException("Create Symbol Table and initialize predefined functions");
    }

    // lookup name in SymbolTable
    public Symbol lookup (String name) throws SymbolNotFoundError {
        Symbol sym = Scopes.get(currentScope).get(name);
        int scope = currentScope;
        while (scope > 0 && sym == null) {
            scope--;

            sym = Scopes.get(scope).get(name);
        }

        if (sym == null) {
            throw new SymbolNotFoundError(name);
        } else {
            System.out.println(sym.name() + sym.type());
            return sym;
        }
        //throw new RuntimeException("implement lookup variable");
    }

    public Symbol lookupInScope(String name, int scope) throws SymbolNotFoundError{
        Symbol sym = Scopes.get(scope).get(name);
        if (sym == null) {
            throw new SymbolNotFoundError(name);
        } else {
            return sym;
        }
    }

    // insert name in SymbolTable
    public Symbol insert (Symbol sym) throws RedeclarationError {
        //If the lookup throws an error, doesn't exist so we can insert
        //Changing this algo
        try{
            this.lookupInScope(sym.name(), currentScope);
            throw new RedeclarationError(sym.name());
        }
        catch(SymbolNotFoundError e){
            return Scopes.get(currentScope).put(sym.name(), sym);
        }

        //throw new RuntimeException("implement insert variable");
    }

    public Symbol insertFunction(Symbol sym) throws RedeclarationError {
        try {
            System.out.println(sym.name());
            Symbol existing = this.lookupInScope(sym.name(), currentScope);
            if (existing.type().toString() == sym.type().toString()) {
                throw new RedeclarationError(sym.name()+sym.type());
            } else {
                return Scopes.get(currentScope).put(sym.name(), sym);
            }


        } catch (SymbolNotFoundError e) {
            return Scopes.get(currentScope).put(sym.name(), sym);
        }
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
