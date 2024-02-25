package types;

public class IntType extends Type {
    @Override
    // arithmetic
    public Type mul (Type that) {
        if(that instanceof IntType){
            return this;
        }
        return new ErrorType("Cannot multiply " + this + " with " + that + ".");
    }

    public Type div (Type that) {
        if(that instanceof IntType){
            return this;
        }
        return new ErrorType("Cannot divide " + this + " by " + that + ".");
    }

    public Type add (Type that) {
        if(that instanceof IntType){
            return this;
        }
        return new ErrorType("Cannot add " + this + " to " + that + ".");
    }

    public Type sub (Type that) {
        if(that instanceof IntType){
            return this;
        }
        return new ErrorType("Cannot subtract " + that + " from " + this + ".");
    }
    //Don't need to implement boolean operators, as super already handles those cases

    
}
