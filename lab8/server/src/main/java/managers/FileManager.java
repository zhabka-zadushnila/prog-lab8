package managers;

import structs.classes.Dragon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager for files, Operates with way on how to save something and open it.
 */
public class FileManager {
    /**
     * Method to save collection to file. Uses Jackson.
     * @param filename name of file where to send
     * @param collection collection which should be stored
     */
    public static void saveCollectionToFile(String filename, Map<String, Dragon> collection){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.writeValue(new File(filename), collection);
            System.out.println("JSON создан успешно.");
        } catch (FileNotFoundException e) {
            System.out.println("Такой директории не существует.");
        } catch (IOException e){
            System.out.println("Произошла неопределённая проблема ввода/вывода.");
        }
    }

    /**
     * Opposite to save. Imports collection from file.
     * @param filename file, from which collection is imported
     * @return returns Map
     */
    public static Map<String, Dragon> importCollectionFromFile(String filename){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.registerModule(new JavaTimeModule());
            Map<String, Dragon> collection = objectMapper.readValue(new File(filename), new TypeReference<Map<String,Dragon>>(){});
            System.out.println("JSON импортирован успешно.");
            return collection;
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден. Создана пустая коллекция (проверьте имя файла).");
        } catch (IOException e){
            System.out.println("Произошла неопределённая проблема ввода/вывода. Создана пустая коллекция (проверьте файл с изначальной коллекцией).");
        }
        return new HashMap<String, Dragon>();
    }


}
