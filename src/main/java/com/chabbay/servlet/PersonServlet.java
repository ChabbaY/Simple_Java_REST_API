package com.chabbay.servlet;

import com.chabbay.data.Person;
import com.chabbay.store.DataStore;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.json.JSONObject;

public class PersonServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        if (name == null) {
            Person[] people = DataStore.getInstance().selectAllPerson();

            response.getOutputStream().println("[");
            boolean isFirst = true;
            for (Person person : people) {
                if (person != null) {
                    StringBuilder json = new StringBuilder();
                    if (!isFirst) {
                        json.append(",{\n");
                    } else {
                        json.append("{\n");
                        isFirst = false;
                    }
                    json.append("\"name\": ")
                            .append(JSONObject.quote(person.getName()))
                            .append(",\n")
                            .append("\"about\": ")
                            .append(JSONObject.quote(person.getAbout()))
                            .append(",\n")
                            .append("\"birthYear\": ")
                            .append(person.getBirthYear())
                            .append("\n")
                            .append("}");
                    response.getOutputStream().println(json.toString());
                }
            }
            response.getOutputStream().println("]");
        } else {
            Person person = DataStore.getInstance().selectPerson(name);

            if(person != null){
                String json = "{\n" +
                        "\"name\": " +
                        JSONObject.quote(person.getName()) +
                        ",\n" +
                        "\"about\": " +
                        JSONObject.quote(person.getAbout()) +
                        ",\n" +
                        "\"birthYear\": " +
                        person.getBirthYear() +
                        "\n" +
                        "}";
                response.getOutputStream().println(json);
            }
            else{
                //Nothing found, so return an empty object. We could also return an error.
                response.getOutputStream().println("{}");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String about = request.getParameter("about");
        int birthYear = Integer.parseInt(request.getParameter("birthYear"));

        //only insert if key doesn't already exist (avoid illegal argument exception)
        if (DataStore.getInstance().selectPerson(name) == null) {
            DataStore.getInstance().insertPerson(new Person(name, about, birthYear));
        } else {
            //Bad Request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String about = request.getParameter("about");
        int birthYear = Integer.parseInt(request.getParameter("birthYear"));

        //update if key exists, else simple insert
        Person old = DataStore.getInstance().selectPerson(name);
        if (old != null) {
            DataStore.getInstance().updatePerson(old, new Person(name, about, birthYear));
        } else {
            DataStore.getInstance().insertPerson(new Person(name, about, birthYear));
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        //only delete if exists (avoid null pointer exception)
        Person old = DataStore.getInstance().selectPerson(name);
        if (old != null) {
            DataStore.getInstance().deletePerson(old);
        } else {
            //Bad Request
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}