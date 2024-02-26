package types;


public class FloatType extends Type {
    @Override
    // arithmetic
    public Type mul (Type that) {
        if(that instanceof FloatType){
            return this;
        }
        return new ErrorType("Cannot multiply " + this + " with " + that + ".");
    }

    public Type div (Type that) {
        if(that instanceof FloatType){
            return this;
        }
        return new ErrorType("Cannot divide " + this + " by " + that + ".");
    }

    public Type add (Type that) {
        if(that instanceof FloatType){
            return this;
        }
        return new ErrorType("Cannot add " + this + " to " + that + ".");
    }

    public Type sub (Type that) {
        if(that instanceof FloatType){
            return this;
        }
        return new ErrorType("Cannot subtract " + that + " from " + this + ".");
    }

    public Type pow (Type that) {
        if (that instanceof FloatType) {
            return this;
        }
        return new ErrorType("Cannot raise " + that + " to " + this + ".");
    }

    public Type mod (Type that) {
        if (that instanceof FloatType) {
            return this;
        }
        return new ErrorType("Cannot get modulus of " + that + " with " + this + ".");
    }
    //Don't need to implement boolean operators, as super already handles those cases

    public String toString() {
        return "float";
    }

}
