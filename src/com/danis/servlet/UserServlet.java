package com.danis.servlet;

import com.danis.dao.UserDao;
import com.danis.util.JSPHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    UserDao userDao = UserDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.setAttribute("users", userDao.findAll());
        req.getRequestDispatcher(JSPHelper.getPath("users")).forward(req, resp);
        //try (var writer = resp.getWriter()) {
            //writer.write(new StringBuilder().append("<h1>").append(userDao.findAll().toString()).append("</h1>").toString());
        //}
    }
}
