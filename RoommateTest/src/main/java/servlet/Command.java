package servlet;

import java.io.Serializable;

public class Command implements Serializable {
    public static final long serialVersionUID = 1;
    private CommandType commandType;
    private Object obj;
    private Object obj2;

    public Object getObj2() {
        return obj2;
    }

    public Command(CommandType type, Object obj) {
        this.commandType = type;
        this.obj = obj;
    }

    public Command(CommandType type, Object obj, Object obj2){
        this.commandType = type;
        this.obj = obj;
        this.obj2 = obj2;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object getObj() {
        return obj;
    }
}
