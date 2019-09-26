package com.landonlib.appengine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landonlib.session.SessionFactory;
import com.landonlib.session.UserInfo;
import com.landonlib.session.UserSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LandonLibApi", value = "/api/landonlib")
public class LandonLibApi extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSession userSession = SessionFactory.logIn("TEST", "TEST");
        UserInfo userInfo = userSession.getUserInfo();

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.write("<html><head><title>User Info</title></head>");
        out.write("<body>");
        out.write("<h1>User Info</h1>");
        out.write("<hr/>");
        out.write("<p>");
        out.write("User Name " + userInfo.getUserName());
        out.write("</p>");
        out.write("<p>");
        out.write("User Id " + userInfo.getUserId());
        out.write("</p>");
        out.write("<p>");
        out.write("Session Token " + userSession.getToken());
        out.write("</p>");
        out.write("</body></html>");
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String userpwd = request.getParameter("userpwd");

        UserSession userSession = SessionFactory.logIn(username, userpwd);
        UserInfo userInfo = userSession.getUserInfo();

        /*response.setContentType("text/plain");
        response.getWriter().print("User Name: " + userInfo.getUserName() + " User Id: " + userInfo.getUserId()
        + " Session Token: " + userSession.getToken());*/
        response.setContentType("application/json");

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("username", userInfo.getUserName());
        responseMap.put("userid", userInfo.getUserId());
        responseMap.put("token", userSession.getToken());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseMap);

        response.getWriter().print(jsonResponse);

    }
}
