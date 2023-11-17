package ru.itis.api.servlets;

import ru.itis.dao.entities.User;
import ru.itis.logic.services.UserService;
import ru.itis.utils.CredentialsValidationUtils;
import ru.itis.utils.HashUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "RegisterServlet", value = "/reg")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String checkbox = Optional.ofNullable(req.getParameter("remember_me")).orElse("off");

        if (CredentialsValidationUtils.isUsernameValid(username)
                && CredentialsValidationUtils.isPasswordValid(password)
                && CredentialsValidationUtils.isEmailValid(email)) {

            String hash = HashUtil.getHash(password);
            User user = userService.get(username, hash);
            if (user == null) {
                userService.create(username, email, hash);
                user = userService.get(username, hash);

                if (checkbox.equals("on")) {
                    Cookie token = new Cookie("token", username + "_" + hash);
                    token.setMaxAge(60 * 60 * 4);
                    resp.addCookie(token);
                }

                session.setAttribute("current", user);
                resp.sendRedirect("/archinterest/news");
            } else {
                req.setAttribute("error", "This user already exists");
                doGet(req, resp);
            }
        } else {
            req.setAttribute("error", "Credentials invalid");
            doGet(req, resp);
        }
    }
}
