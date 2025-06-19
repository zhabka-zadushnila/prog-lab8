package managers;

import structs.Packet;
import structs.classes.Dragon;
import utils.RequestConstructor;
import utils.RequestResponseTool;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;

/**
 * Manager that manages collection
 */
public class CollectionManager {

    private final java.time.LocalDate initTime;
    Map<String, Dragon> collection = FXCollections.observableHashMap();
    static int MAX_RECONNECT_ATTEMPTS = 5;
    static int RECONNECT_TIMEOUT = 2000; //millis
    String hostname = "188.242.233.237";
    int port = 52947;
    SocketChannel channel = ConnectionManager.connectToServer(hostname, port, MAX_RECONNECT_ATTEMPTS, RECONNECT_TIMEOUT);



    /**
     * May be initiated with no specific params
     */
    public CollectionManager() {
        this.initTime = java.time.LocalDate.now();
    }

    /**
     * May be initiated with JSON file. Uses {@link FileManager} for collection import.
     *
     * @param filename String of filename
     */
    public CollectionManager(String filename) {
        this();
        this.collection = FileManager.importCollectionFromFile(filename);
    }

    /**
     * Method that just creates a new empty collection
     */
    public void clearCollection() {
        this.collection = new HashMap<String, Dragon>();
    }

    /**
     * Used to return map info
     *
     * @return returns {@link Map}.
     */
    public Map<String, Object> getCollectionInfoMap() {
        Map<String, Object> tmpMap = new HashMap<>();
        tmpMap.put("Type", "HashMap");
        tmpMap.put("Date", this.initTime);
        tmpMap.put("ElementsQuantity", collection.size());
        return tmpMap;
    }

    /**
     * Adds element
     *
     * @param id     String, aka "key" in map
     * @param dragon Dragon object
     * @return returns result of operation. True, if element was added, and false if there is already one in collection.
     */
    public boolean addElement(String id, Dragon dragon) {
        if (collection.containsKey(id)) {
            return false;
        } else {
            collection.put(id, dragon);
            return true;
        }
    }

    public boolean addElement(Map.Entry<String, Dragon> element) {
        return addElement(element.getKey(), element.getValue());
    }

    /**
     * Replaces element
     *
     * @param id     String, aka "key" for Map
     * @param dragon {@link Dragon} object
     */
    public void replaceElement(String id, Dragon dragon) {
        collection.replace(id, dragon);
    }

    /**
     * Some sort of wrapper for remove
     *
     * @param id String, aka "key" for Map
     */
    public void killElement(String id) {
        try {
            collection.remove(id);
        } catch (Throwable e) {
            System.out.println(e);
        }
    }

    /**
     * Small wrapper for Map get() method
     *
     * @param id String key
     * @return returns dragon
     */
    public Dragon getElement(String id) {
        return collection.get(id);
    }

    public boolean hasElement(String id) {
        return collection.containsKey(id);
    }

    public Map<String, Dragon> getCollection() {
        return collection;
    }

    public void setCollection(Map<String, Dragon> collection) {
        this.collection = collection;
    }

    public void sync(){
        Packet packet = RequestConstructor.createRequest();
        try {
            RequestResponseTool.sendPacket(channel, packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        packet = RequestResponseTool.getPacket(channel);
        if(packet == null){
            System.out.println("somehow packet in sync is null");
            return;
        }
        Map<String, Dragon> updatedCollection = packet.getMap();
        this.collection = updatedCollection;

    }

}
