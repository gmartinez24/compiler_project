package co2;

public class Symbol {

    private String name;

    private types.Type type;

    private int lineNumber;

    private int charPosition;

    // TODO: Add other parameters like type

    public Symbol ( types.Type type, String name, int lineNumber, int charPosition) {
        this.name = name;
        this.type = type;
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
    }
    public String name () {
        return name;
    }

    public types.Type type() {return type;}

    public int lineNumber() {return lineNumber;}

    public int charPosition() {return charPosition;}

    public String toString() {
        return "(" + name +":" + type +")";
    }

}
