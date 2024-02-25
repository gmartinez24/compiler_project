package co2;

public class Symbol {

    private String name;

    private String type;


    public Symbol ( String type, String name) {
        this.name = name;
        this.type = type;
    }
    public String name () {
        return name;
    }

    public String type() {return type;}

    public String toString() {
        return "(" + name +":" + type +")";
    }

}
