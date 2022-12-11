package com.chabbay.store;

import com.chabbay.data.Person;

import java.util.HashMap;
import java.util.Map;

/**
 * example data storage, can be replaced with database
 *
 * @author Linus Englert
 */
public class DataStore {
    private final Map<String, Person> personMap = new HashMap<>();

    /**
     * this class is a singleton and should not be instantiated directly!
     */
    private static final DataStore instance = new DataStore();
    public static DataStore getInstance(){
        return instance;
    }

    /**
     * private constructor so people know to use the getInstance() function instead
     */
    private DataStore() {
        //initialize DB
        DataBaseController controller = DataBaseController.getInstance();
        controller.init();
        controller.test();
        //initial data
        personMap.put("Ada", new Person("Ada", "Ada Lovelace was the first programmer.", 1815));
    }

    public Person[] selectAllPerson() {
        return personMap.values().toArray(Person[]::new);
    }
    public Person selectPerson(String name) {
        return personMap.get(name);
    }
    public void insertPerson(Person person) {
        personMap.put(person.getName(), person);
    }
    public void updatePerson(Person old, Person person) {
        personMap.replace(old.getName(), old, person);
    }
    public void deletePerson(Person person) {
        personMap.remove(person.getName());
    }
}