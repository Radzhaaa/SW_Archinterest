package ru.itis.api.servlets;

import ru.itis.dao.entities.*;
import ru.itis.logic.services.ProjectService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ProjectCommentServlet", value = "/project/comment/*")
public class ProjectCommentServlet extends HttpServlet {

    private ProjectService projectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.projectService = (ProjectService) config.getServletContext().getAttribute("projectService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String referer = req.getHeader("Referer");

        User current = (User) session.getAttribute("current");
        String content = req.getParameter("content");

        ProjectComment comment = ProjectComment.builder()
                .authorId(current.getId())
                .authorUsername(current.getUsername())
                .avatarPath(current.getAvatarPath() == null ? "https://bootdey.com/img/Content/avatar/avatar7.png" : current.getAvatarPath())
                .content(content)
                .build();

        Long projectId = Long.parseLong(req.getPathInfo().replace("/", ""));
        Project project = projectService.get(projectId);

        if (project != null && comment != null) {
            projectService.update(project, comment);
        }

        resp.sendRedirect(referer);
    }
}
