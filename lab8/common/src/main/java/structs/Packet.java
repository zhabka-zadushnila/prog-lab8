package structs;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

import structs.classes.Dragon;
import structs.wrappers.DragonDisplayWrapper;


public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    User user;
    PacketType packetType;
    String command;
    String[] arguments;
    Object object;
    String text;
    Map<String, Dragon> map;

    public Packet(PacketType packetType, String command, String[] arguments, User user) {
        this(packetType, command, arguments, null, user);
    }

    public Packet(PacketType packetType, String command, String[] arguments, Object object, User user) {
        this.packetType = packetType;
        this.command = command;
        this.arguments = arguments;
        this.object = object;
        this.user = user;
    }

    public Packet(PacketType packetType, String command, String[] arguments, Object object, String text, User user) {
        this.packetType = packetType;
        this.command = command;
        this.arguments = arguments;
        this.object = object;
        this.text = text;
        this.user = user;
    }

    public Packet(PacketType packetType, Map<String, Dragon> map){
        this.map = map;
        this.packetType = packetType;
    }

    public User getUser() {
        return user;
    }

    public Map<String, Dragon> getMap(){
        return map;
    }

    public AbstractMap.SimpleEntry<String, Object> getArgsObjectEntry() {
        try {
            return new AbstractMap.SimpleEntry<>(arguments[0], object);
        } catch (Exception e) {
            return null;
        }

    }

    public DragonDisplayWrapper getArgsObjectWrapper() {
        return (DragonDisplayWrapper) object;
    };

    public String getCommand() {
        return command;
    }

    public Object getArguments() {
        return arguments;
    }

    public String getText() {
        return text;
    }

    public boolean isText() {
        return packetType == PacketType.TEXT;
    }

    public boolean isCommand() {
        return packetType == PacketType.ARGS_COMMAND;
    }

    public boolean isObjectCommand() {
        return packetType == PacketType.OBJECT_COMMAND;
    }

    public boolean isMap(){
        return packetType == PacketType.MAP;
    }


    @Override
    public String toString() {
        if (packetType == PacketType.TEXT) {
            return text;
        }
        if (packetType == PacketType.ARGS_COMMAND || packetType == PacketType.OBJECT_COMMAND) {
            return command;
        }
        return "unknown";
    }
}
