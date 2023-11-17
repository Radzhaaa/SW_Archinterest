package ru.itis.api.servlets;

import ru.itis.dao.entities.User;
import ru.itis.logic.services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "MainPageServlet", value = "/arch")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");
        if (current != null) {
            req.setAttribute("current", current);
        }
        resp.setContentType("text/html");
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/main.html").forward(req, resp);
    }
}
