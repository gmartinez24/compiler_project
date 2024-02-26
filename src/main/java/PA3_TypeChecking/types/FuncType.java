package types;

public class FuncType extends Type {
    private  TypeList params;
    private  Type returnType;
    private  String name;

    public FuncType () {
        this.params = new TypeList();

    }

    public String getName(){
        return name;
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

    public Type call(TypeList args){
        if(params.length() != args.length()){
            return new ErrorType("Cannot call " + this + " using " + args + ".");
        }
        int count = 0;
        for(int i = 0; i < args.length(); i++){
            if(!params.at(i).getClass().equals( args.at(i).getClass())){
                return new ErrorType("Call with args " + args.toString() + " matches multiple function signatures.");
            }
        }
        return returnType;

    }
}
