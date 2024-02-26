package types;

public class BoolType extends Type {
    public String toString() {
        return "bool";
    }

    public Type and (Type that) {
        if(that instanceof BoolType){
            return that;
        }
        return new ErrorType("Cannot compute " + this + " and " + that + ".");
    }

    public Type or (Type that) {
        if(that instanceof BoolType){
            return that;
        }
        return new ErrorType("Cannot compute " + this + " or " + that + ".");
    }

    public Type not () {
        return this;
    }

}
