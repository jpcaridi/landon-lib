package com.landonlib.appengine.api;

import com.landonlib.service.consumer.LastFmService;
import com.landonlib.session.SessionFactory;
import com.landonlib.session.UserSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SearchApi", value = "/api/search")
public class SearchApi extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LastFmService lastFmService = new LastFmService();

        response.setContentType("application/json");
        response.getWriter().print(lastFmService.search("nevermind"));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LastFmService lastFmService = new LastFmService();

        String userid= request.getParameter("userid");
        String token = request.getParameter("token");
        String searchString = request.getParameter("searchstr");

        UserSession userSession = SessionFactory.getSession(userid, token);

        response.setContentType("application/json");
        response.getWriter().print(lastFmService.search(userSession, searchString));

    }
}
