package types;

public class FuncType extends Type {
    private  TypeList params;
    private  Type returnType;
    private  String name;

    public FuncType () {
        this.params = new TypeList();

    }
    public void setName(String name){
        this.name = name;
    }

    public void setParams(TypeList params){
        this.params = params;
    }

    public void setReturnType(Type returnType){
        this.returnType = returnType;
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
        if(args == null && params.length() == 0){
            return returnType;
        }
        else if(args == null){
            return new ErrorType("error");
        }
        if(args.length() != params.length()){
            return new ErrorType("error");
        }

        for(int i = 0; i < args.length(); i++){
            if(!params.at(i).getClass().equals( args.at(i).getClass())){
                return new ErrorType("error here");
            }
        }
        return returnType;

    }

    public Type getReturnType() {
        return returnType;
    }

    public TypeList getParams(){
        return params;
    }
}
