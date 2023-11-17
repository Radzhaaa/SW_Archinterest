package ru.itis.api.servlets;

import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.TagService;
import ru.itis.logic.services.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "PreferencesServlet", value = "/preferences")
public class PreferencesServlet extends HttpServlet {
    private UserService userService;
    private TagService tagService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
        this.tagService = (TagService) servletContext.getAttribute("tagService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();

        User current = (User) session.getAttribute("current");
        List<String> userTagNames = tagService.getList(current).stream()
                .map(Tag::getTitle)
                .toList();
        List<Tag> allTags = tagService.getAll();

        req.setAttribute("profile", current);
        req.setAttribute("allTags", allTags);
        req.setAttribute("userTagNames", userTagNames);

        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/preferences.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");

        current.setName(req.getParameter("name"));
        current.setPatronymic(req.getParameter("patronymic"));
        current.setEmail(req.getParameter("email"));
        current.setLastname(req.getParameter("lastName"));
        current.setAbout(req.getParameter("about"));

        update(req, current);
        User user = userService.get(current.getId());

        session.setAttribute("current", user);
        resp.sendRedirect("/archinterest/profile");
    }

    private void update(HttpServletRequest req, User profile) {
        List<Tag> tags = tagService.getAll();
        List<Tag> tagsToUpdate = new ArrayList<>();

        for (int i = 1; i <= tags.size(); i++) {
            String checkbox = Optional.ofNullable(req.getParameter("tag-" + i)).orElse("off");

            if (checkbox.equals("on")) {
                tagsToUpdate.add(tags.get(i - 1));
            }
        }

        userService.update(profile, tagsToUpdate);
    }
}
