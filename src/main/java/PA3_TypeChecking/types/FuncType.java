package types;

public class FuncType extends Type {
    // last element in params in return type
    private TypeList params;
    private Type returnType;

    public FuncType () {
        params = new TypeList();
        //returnType = params.at(params.length() - 1);
    }

    public TypeList params() {
        return this.params;
    }
    public String toString() {
        String type = "(";
        for ( int i = 0; i < params.length() - 1; i++) {
            type += params.at(i).toString() + ",";
        }
        if (type.length() > 1) {
            type = type.substring(0, type.length()-1);
        }

        type += ")";
        if (params.length() >= 1) {
            returnType = params.at(params.length() - 1);
        } else {
            returnType = new VoidType();
        }

        return type + "->" + returnType.toString();
    }
}
