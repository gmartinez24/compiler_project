package types;

import java.util.ArrayList;
import java.util.List;

public class ArrayType extends Type {

    Type elementType;

    // each dimension of the array is an index in the list where the value at an index is the length
    List<Integer> dimensions;

    // function call
    public ArrayType() {

    }
    public Type elementType() {
        return elementType;
    }

    public ArrayType (Type elementType, List<Integer> dimensions) {
        this.dimensions = dimensions;
        this.elementType = elementType;
    }

    public String toString() {
        String type = "";
        for (Integer dim : dimensions) {
            type+="[]";
        }
        return elementType.toString() + type;
    }


}
