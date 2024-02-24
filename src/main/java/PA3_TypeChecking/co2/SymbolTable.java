package co2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {

    // TODO: Create Symbol Table structure
    // Make a Hashtable with the key being the variable name and the value being the type
    // question is about scope
    List<HashMap<String, Symbol>> Scopes;

    int currentScope;
    // create a stack of scopes
    // each scope is a hashtable as descibed above
    public SymbolTable () {

        Scopes = new ArrayList<>();
        HashMap<String, Symbol> globalScope = new HashMap<>();
        Scopes.add(globalScope);
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
            return sym;
        }
        //throw new RuntimeException("implement lookup variable");
    }

    // insert name in SymbolTable
    public Symbol insert (Symbol sym) throws RedeclarationError {
        if (this.lookup(sym.name()) != null) {
            throw new RedeclarationError(sym.name());
        } else {
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
