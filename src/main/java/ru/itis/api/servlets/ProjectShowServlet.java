package ru.itis.api.servlets;

import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.ProjectComment;
import ru.itis.dao.entities.Tag;
import ru.itis.logic.services.CommentService;
import ru.itis.logic.services.ProjectService;
import ru.itis.logic.services.TagService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProjectShowServlet", value = "/projects/project/*")
public class ProjectShowServlet extends HttpServlet {
    private ProjectService projectService;
    private CommentService commentService;
    private TagService tagService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.projectService = (ProjectService) servletContext.getAttribute("projectService");
        this.commentService = (CommentService) servletContext.getAttribute("commentService");
        this.tagService = (TagService) servletContext.getAttribute("tagService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        Long projectId = Long.parseLong(pathInfo.replace("/", ""));
        Project project = projectService.get(projectId);
        List<ProjectComment> comments = commentService.getList(project);
        List<Tag> tags = tagService.getList(project);

        req.setAttribute("project", project);
        req.setAttribute("comments", comments);
        req.setAttribute("tags", tags);
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/single_project.html").forward(req, resp);
    }
}
