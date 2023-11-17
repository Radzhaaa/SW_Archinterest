package ru.itis.api.servlets;

import ru.itis.dao.entities.User;
import ru.itis.logic.services.UserService;
import ru.itis.utils.HashUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "AuthServlet", value = "/auth")
public class AuthorizationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/auth.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String checkbox = Optional.ofNullable(req.getParameter("remember_me")).orElse("off");

        String hash = HashUtil.getHash(password);
        User user = userService.get(username, hash);

        if (user != null) {
            if (checkbox.equals("on")) {
                Cookie token = new Cookie("token", username + "_" + hash);
                token.setMaxAge(60*60*4);
                resp.addCookie(token);
            }

            session.setAttribute("current", user);
            resp.sendRedirect("/archinterest/arch");
        } else {
            req.setAttribute("error", "User not found :(");
            doGet(req, resp);
        }
    }
}