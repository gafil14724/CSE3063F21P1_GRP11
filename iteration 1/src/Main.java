import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {


        StudentId my = new StudentId(new Student("baris", "hazar", 3, 19));
        System.out.println(my);

        Grade grade = new Grade(new Course("CSE3055", "name", "fall", 21, 3, 2, null), 50);
        System.out.println(grade.getLetterGrade());



       /* ArrayList<String> names = new ArrayList<>();
        ArrayList<String> surnames = new ArrayList<>();


        JSONParser parser = new JSONParser();
        Object nameObj = parser.parse(new FileReader("names.json"));
        JSONObject nameJson =  (JSONObject) nameObj;
        JSONArray name = (JSONArray) nameJson.get("names");
        Iterator<String> iterator = name.iterator();
        while (iterator.hasNext()) {
            names.add(iterator.next());
        }


        Object surnameObj = parser.parse(new FileReader("surnames.json"));
        JSONObject surnameJson =  (JSONObject) surnameObj;
        JSONArray surname = (JSONArray) surnameJson.get("surnames");
        Iterator<String> iterator2 = surname.iterator();
        while (iterator2.hasNext()) {
            surnames.add(iterator2.next());
        }

        System.out.println(surnames);*/

       /* File names = new File("names.txt");
        List<String> list = new ArrayList<String>();

        Scanner sc = new Scanner(names);
        while (sc.hasNext()) {
            list.add(sc.nextLine());
        }

        for (String s : list){
            System.out.println(s);
        }

        // this method converts a list to JSON Array
        String jsonStr = JSONArray.toJSONString(list);
        System.out.println(jsonStr);*/

    }
}
