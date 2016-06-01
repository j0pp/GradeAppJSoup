

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    public static ArrayList<Double> gradeNumber = new ArrayList<>();
    public static ArrayList<String> gradeLetter = new ArrayList<>();

    public static ArrayList<String> teacherList = new ArrayList<>();
    public static ArrayList<String> coursesList = new ArrayList<>();



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


        //System.out.println(doc);

        //Get persons name
        String name = doc.select("div#sps-stdemo-non-conf").select("h1").first().text();
        System.out.println(name);

        //Getting Grades for Semester 2
        Elements grades = doc.select("td.colorMyGrade").select("[href*='fg=S2']");
        for(Element j : grades) {

            if(!j.text().equals("--")) {
                String gradeText = j.text();
                gradeLetter.add(gradeText.substring(0, gradeText.indexOf(" ")));
                gradeNumber.add(Double.parseDouble(gradeText.substring(gradeText.indexOf(" ") + 1)));
            }
        }

        Elements teachers = doc.select("td[align='left']");
        for(int i = 1; i < teachers.size(); i+=2) {

            String fullText = teachers.get(i).text().replaceAll("//s+", ".");

            System.out.println(fullText);

            teacherList.add(fullText);
            //String classText = fullText.substring(0, fullText.indexOf("   "));
            //String teacherText = fullText.substring(fullText.indexOf("   ") + 3);

            //teacherList.add(teacherText);
            //coursesList.add(classText);


        }

        for(int i = 0; i < gradeLetter.size(); i++) {

            System.out.println(teacherList.get(i) + " | Grade: " + gradeLetter.get(i) + " (" + gradeNumber.get(i) + ")");
        }

        //for(int p = 0; p < gradeLetter.size(); p++) {
            //System.out.println(teacherList.get(p) + "/ " + teacherList.get(p) + ": " + gradeLetter.get(p) + "/ " + gradeNumber.get(p));
        //}
        //System.out.println(teacherList);


        //System.out.println(gradeLetter);
        //System.out.println(gradeNumber);

        System.out.println(doc.body());


    }


}

