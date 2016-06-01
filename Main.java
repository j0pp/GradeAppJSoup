package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static final String USERNAME = "BAL_jfbeaubien";
    public static final String PASSWORD = "monkeyfarts";

    public static final String URL = "https://ps.seattleschools.org/public/";
    public static final String POST_URL = "https://ps.seattleschools.org/guardian/home.html";

    public static final String userAgent = "\"Mozilla/5.0 (Windows NT\" +\n" +
            "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";

    public static ArrayList<String> gradeNumber = new ArrayList<>();
    public static ArrayList<Character> gradeLetter = new ArrayList<>();

    public static ArrayList<String> teachers = new ArrayList<>();
    public static ArrayList<String> courses = new ArrayList<>();



    public static void main(String[] args) throws IOException {

        HashMap<String, String> cookies = new HashMap<>();
        HashMap<String, String> formData = new HashMap<>();

        Connection.Response loginForm = Jsoup.connect(URL)
                .method(Connection.Method.GET)
                .userAgent(userAgent)
                .execute();

        Document loginDoc = loginForm.parse();

        String pstoken = loginDoc.select("#LoginForm > input[type=\"hidden\"]:nth-child(1)").first().attr("value");
        String contextData = loginDoc.select("#contextData").first().attr("value");
        String dbpw = loginDoc.select("#LoginForm > input[type=\"hidden\"]:nth-child(3)").first().attr("value");
        String serviceName = "PS Parent Portal";
        String credentialType = "User Id and Password Credential";


        cookies.putAll(loginForm.cookies());


        //Inserting all hidden form data things
        formData.put("pstoken", pstoken);
        formData.put("contextData", contextData);
        formData.put("dbpw", dbpw);
        formData.put("serviceName", serviceName);
        formData.put("credentialType", credentialType);
        formData.put("Account", USERNAME);
        formData.put("ldappassword", PASSWORD);
        formData.put("pw", PASSWORD);


         Connection.Response homePage = Jsoup.connect(POST_URL)
                 .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(userAgent)
                .execute();



        Document doc = Jsoup.parse(homePage.parse().html());


        System.out.println(doc);

        /*

        //Get ALL Semester 2 grades + letters
        Elements grades = doc.select("#tblgrades tr").select("[headers='tblgr-s2']");

        System.out.println(grades);

        for(int i = 1; i < grades.size(); i += 2) {
            gradeNumber.add(grades.get(i).text());
        }

        //Get ALL Teachers
        Elements teacherElements = doc.select("#tblgrades tr").select("[headers='tblgr-course']");
        for(int i = 1; i < teacherElements.size(); i += 2) {
            teachers.add(teacherElements.get(i).text());
        }

        for(int j = 0; j < teachers.size(); j++) {
            System.out.print(teachers.get(j) + " | Grade: ");
            System.out.println(gradeNumber.get(j));
            System.out.println();
        }

        double GPA = 0;

        for(String s : gradeNumber) {
            GPA += Double.parseDouble(s.substring(s.indexOf(" ") + 1));
        }

        System.out.println("Average: " + GPA / (gradeNumber.size()));

        */


    }


}

