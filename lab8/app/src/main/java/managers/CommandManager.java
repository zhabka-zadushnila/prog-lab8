package managers;
import commands.BasicCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that manages all the commands. Actually, that's some sort of a wrapper for Map ^_^
 * Maybe got no sense but still I love it and I think it is some sort of Controller in MVC.
 *
 *
*/
public class CommandManager {
    private final Map<String, BasicCommand> localCommandsMap = new HashMap<String, BasicCommand>();
    private final Map<String, BasicCommand> serverCommandsMap = new HashMap<String, BasicCommand>();
    CollectionManager collectionManager;
    ClientManager clientManager;

    public CommandManager(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    /**
     * Adds command in command manager.
     * @param command object of {@link BasicCommand} children
     */
    public void addLocalCommand(BasicCommand command) {
        this.localCommandsMap.put(command.getName(), command);
    }
    public void addServerCommand(BasicCommand command) {
        this.serverCommandsMap.put(command.getName(), command);
    }

    public boolean hasCommand(String command){
        return localCommandsMap.containsKey(command) || serverCommandsMap.containsKey(command);
    }

    public boolean isLocalCommand(String command){
        return localCommandsMap.containsKey(command);
    }


    public Map<String, BasicCommand> getLocalCommandsMap() {
        return this.localCommandsMap;
    }
    public Map<String, BasicCommand> getServerCommandsMap() {
        return this.serverCommandsMap;
    }

    public CollectionManager getCollectionManager(){
        return this.collectionManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }


    public BasicCommand getCommand(String name){
        if(localCommandsMap.get(name) == null){
            return serverCommandsMap.get(name);
        }
        return localCommandsMap.get(name);
    }


}
