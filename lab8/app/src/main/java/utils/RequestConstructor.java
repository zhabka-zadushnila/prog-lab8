package utils;

import java.util.Map;
import java.util.TreeMap;

import commands.BasicCommand;
import structs.Packet;
import structs.PacketType;
import structs.User;
import structs.classes.Dragon;
import structs.wrappers.DragonDisplayWrapper;

public class RequestConstructor {

    public static Packet createRequest(BasicCommand command, String[] args, Object object, User user) {
        if (object == null) {
            return new Packet(PacketType.ARGS_COMMAND, command.getName(), args, user);
        } else if (object instanceof Dragon) {
            return new Packet(PacketType.OBJECT_COMMAND, command.getName(), args, object, user);
        } else if (object instanceof User) {
            return new Packet(PacketType.OBJECT_COMMAND, command.getName(), args, (User) object);
            //was PacketType.AUTH
        } else if (object instanceof DragonDisplayWrapper){
            return new Packet(PacketType.OBJECT_COMMAND, command.getName(), args, object, user);
        }

        return null;
    }
///метод для запроса актуальной версии коллекции
    public static Packet createRequest(){
        return new Packet(PacketType.MAP, new TreeMap<>());
    }
}
