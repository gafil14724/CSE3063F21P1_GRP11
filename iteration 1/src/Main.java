import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {


        RegistrationSystem x = new RegistrationSystem("fall");




    }

    public static ArrayList<String> getNamesList() throws IOException, ParseException {
        ArrayList<String> names = new ArrayList<>();

        JSONParser parser = new JSONParser();
        Object nameObj = parser.parse(new FileReader("names.json"));
        JSONObject nameJson =  (JSONObject) nameObj;
        JSONArray name = (JSONArray) nameJson.get("names");
        Iterator<String> iterator = name.iterator();
        while (iterator.hasNext()) {
            names.add(iterator.next());
        }

        return names;
    }

    public static ArrayList<String> getSurnamesList() throws IOException, ParseException {
        ArrayList<String> surnames = new ArrayList<>();

        JSONParser parser = new JSONParser();
        Object surnameObj = parser.parse(new FileReader("surnames.json"));
        JSONObject surnameJson =  (JSONObject) surnameObj;
        JSONArray surname = (JSONArray) surnameJson.get("surnames");
        Iterator<String> iterator2 = surname.iterator();
        while (iterator2.hasNext()) {
            surnames.add(iterator2.next());
        }

        return surnames;
    }
}
