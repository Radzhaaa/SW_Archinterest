package ru.itis.api.servlets;

import ru.itis.dao.entities.Image;
import ru.itis.dao.entities.News;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.ImageService;
import ru.itis.logic.services.NewsService;
import ru.itis.logic.services.TagService;
import ru.itis.logic.services.UserService;

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

@WebServlet(name = "NewsCreateServlet", value = "/news/create")
@MultipartConfig
public class NewsCreateServlet extends HttpServlet {
    private NewsService newsService;
    private ImageService imageService;
    private TagService tagService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.newsService = (NewsService) servletContext.getAttribute("newsService");
        this.userService = (UserService) servletContext.getAttribute("userService");
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
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/news_create.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");

        Part file = req.getPart("image");
        imageService.create(file);
        Image image = imageService.get(file);

        News news = News.builder()
                .title(req.getParameter("title"))
                .annotation(req.getParameter("annotation"))
                .content(req.getParameter("content"))
                .coverPath(image.getFilePath())
                .authorId(current.getId())
                .authorUsername(current.getUsername())
                .authorLastname(current.getLastname())
                .authorName(current.getName())
                .build();

        newsService.create(news);
        News savedNews = newsService.get(news);
        update(req, savedNews);

        resp.sendRedirect("/archinterest/news");
    }

    private void update(HttpServletRequest req, News news) {
        List<Tag> tags = tagService.getAll();
        List<Tag> tagsToUpdate = new ArrayList<>();

        for (int i = 1; i <= tags.size(); i++) {
            String checkbox = Optional.ofNullable(req.getParameter("tag-" + i)).orElse("off");

            if (checkbox.equals("on")) {
                tagsToUpdate.add(tags.get(i - 1));
            }
        }

        newsService.update(news, tagsToUpdate);
    }
}
