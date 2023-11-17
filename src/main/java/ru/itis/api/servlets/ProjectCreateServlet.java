package ru.itis.api.servlets;

import ru.itis.dao.entities.Image;
import ru.itis.dao.entities.Project;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.ImageService;
import ru.itis.logic.services.ProjectService;
import ru.itis.logic.services.TagService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ProjectCreateServlet", value = "/projects/create")
@MultipartConfig
public class ProjectCreateServlet extends HttpServlet {
    private ProjectService projectService;
    private ImageService imageService;
    private TagService tagService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.projectService = (ProjectService) servletContext.getAttribute("projectService");
        this.tagService = (TagService) servletContext.getAttribute("tagService");
        this.imageService = (ImageService) servletContext.getAttribute("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");
        List<Tag> tags = tagService.getAll();
        req.setAttribute("tags", tags);
        req.setAttribute("author", current.getId());
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/project_create.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");

        Part file = req.getPart("image");
        imageService.create(file);
        Image image = imageService.get(file);

        Project project = Project.builder()
                .title(req.getParameter("title"))
                .content(req.getParameter("content"))
                .year(Integer.parseInt(req.getParameter("year")))
                .area(Double.parseDouble(req.getParameter("area")))
                .address(req.getParameter("address"))
                .coverPath(image.getFilePath())
                .authorId(current.getId())
                .authorUsername(current.getUsername())
                .build();

        projectService.create(project);
        Project savedProject = projectService.get(project);
        update(req, savedProject);

        resp.sendRedirect("/archinterest/projects");
    }

    private void update(HttpServletRequest req, Project project) {
        List<Tag> tags = tagService.getAll();
        List<Tag> tagsToUpdate = new ArrayList<>();

        for (int i = 1; i <= tags.size(); i++) {
            String checkbox = Optional.ofNullable(req.getParameter("tag-" + i)).orElse("off");

            if (checkbox.equals("on")) {
                tagsToUpdate.add(tags.get(i - 1));
            }
        }

        projectService.update(project, tagsToUpdate);
    }
}
