package types;

public class ErrorType extends Type {



    private String message;

    public ErrorType (String msg) {
        this.message = msg;
    }

    public String toString(){
        return "ErrorType(" + message + ")";
    }

    public String message() {
        return message;
    }
}
