package ru.itis.api.servlets;

import ru.itis.dao.entities.User;
import ru.itis.logic.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AvatarChangeServlet", value = "/avatar")
@MultipartConfig
public class AvatarChangeServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.userService = (UserService) config.getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String referer = req.getHeader("Referer");
        Part file = req.getPart("image");
        User current = (User) session.getAttribute("current");
        userService.update(current, file);

        User user = userService.get(current.getId());
        session.setAttribute("current", user);
        resp.sendRedirect(referer);
    }
}
