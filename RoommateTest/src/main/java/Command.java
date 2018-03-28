import java.io.Serializable;

public class Command implements Serializable {
    public static final long serialVersionUID = 1;
    private CommandType commandType;
    private Object obj;

    public Command(CommandType type, Object obj) {
        this.commandType = type;
        this.obj = obj;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object getObj() {
        return obj;
    }
}
