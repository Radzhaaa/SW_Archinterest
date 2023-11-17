package ru.itis.api.servlets;

import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.ProjectService;
import ru.itis.logic.services.TagService;
import ru.itis.logic.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {

    private UserService userService;
    private TagService tagService;
    private ProjectService projectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
        this.tagService = (TagService) servletContext.getAttribute("tagService");
        this.projectService = (ProjectService) servletContext.getAttribute("projectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();

        User profile = (User) session.getAttribute("current");
        List<Tag> tags = tagService.getList(profile);
//        List<Project> projects = projectService.getList(profile);

        req.setAttribute("profile", profile);
        req.setAttribute("tags", tags);

        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/profile.html").forward(req, resp);
    }
}
