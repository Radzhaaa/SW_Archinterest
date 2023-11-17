package ru.itis.api.servlets;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.Tag;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.CommentService;
import ru.itis.logic.services.NewsService;
import ru.itis.logic.services.TagService;

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

@WebServlet(name = "SingleNewsServlet", value = "/news/newspiece/*")
public class SingleNewsServlet extends HttpServlet {

    private NewsService newsService;
    private CommentService commentService;
    private TagService tagService;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        this.newsService = (NewsService) servletContext.getAttribute("newsService");
        this.commentService = (CommentService) servletContext.getAttribute("commentService");
        this.tagService = (TagService) servletContext.getAttribute("tagService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String pathInfo = req.getPathInfo();
        Long newsId = Long.parseLong(pathInfo.replace("/", ""));
        News news = newsService.get(newsId);
        List<NewsComment> comments = commentService.getList(news);
        List<Tag> tags = tagService.getList(news);

        req.setAttribute("post", news);
        req.setAttribute("comments", comments);
        req.setAttribute("tags", tags);
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/single_news.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");

        if (current != null) {
            News news = (News) req.getAttribute("news");
            newsService.create(news);
        }

        req.getServletContext().getRequestDispatcher("/news").forward(req, resp);
    }
}
