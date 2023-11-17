package ru.itis.api.servlets;

import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.ProjectService;

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

@WebServlet(name = "ProjectsBoardServlet", value = "/projects")
public class ProjectsBoardServlet extends HttpServlet {
    private ProjectService projectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.projectService = (ProjectService) servletContext.getAttribute("projectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");
        List<Project> interested = projectService.getInterested(current);
        req.setAttribute("interested", interested);
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/projects.html").forward(req, resp);
    }
}
