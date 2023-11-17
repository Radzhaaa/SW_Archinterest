package ru.itis.api.servlets;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.NewsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "NewsCommentServlet", value = "/news/comment/*")
public class NewsCommentServlet extends HttpServlet {

    private NewsService newsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.newsService = (NewsService) config.getServletContext().getAttribute("newsService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String referer = req.getHeader("Referer");

        User current = (User) session.getAttribute("current");
        String content = req.getParameter("content");

        NewsComment comment = NewsComment.builder()
                .authorId(current.getId())
                .authorUsername(current.getUsername())
                .avatarPath(current.getAvatarPath() == null ? "https://bootdey.com/img/Content/avatar/avatar7.png" : current.getAvatarPath())
                .content(content)
                .build();

        Long newsId = Long.parseLong(req.getPathInfo().replace("/", ""));
        News news = newsService.get(newsId);

        if (news != null && comment != null) {
            newsService.update(news, comment);
        }

        resp.sendRedirect(referer);
    }
}
