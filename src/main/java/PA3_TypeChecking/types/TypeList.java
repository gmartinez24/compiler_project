package types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TypeList extends Type implements Iterable<Type> {

    private List<Type> list;

    public TypeList () {
        list = new ArrayList<>();
    }

    public void append (Type type) {
        list.add(type);
    }

    public List<Type> getList () {
        return list;
    }

    @Override
    public Iterator<Type> iterator () {
        return list.iterator();
    }

    //TODO more helper here
    public int length() {
        return list.size();
    }

    public Type at(int index) {
        return list.get(index);
    }


    public String toString(){
        String str = "TypeList(";
        for(Type type: list){
            str += type.toString();
            str += ",";
        }
        str = str.substring(0, str.length() -1);
        str += ")";
        return str;
    }
}
