package ru.itis.api.servlets;

import ru.itis.dao.entities.News;
import ru.itis.dao.entities.NewsComment;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.CommentService;
import ru.itis.logic.services.NewsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "NewsServlet", value = "/news")
public class NewsServlet extends HttpServlet {

    private NewsService newsService;
    private CommentService commentService;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        this.newsService = (NewsService) servletContext.getAttribute("newsService");
        this.commentService = (CommentService) servletContext.getAttribute("commentService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();
        User current = (User) session.getAttribute("current");
        Cookie[] cookies = req.getCookies();
        boolean today = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("today")) {
                today = cookie.getValue().equals("on");
            }
        }


        List<News> news;

        if (current != null) {
            if (today) {
                req.setAttribute("today", true);
                news = newsService.getAllToday(current);
            } else {
                news = newsService.getAll(current);
            }
        } else {
            if (today) {
                req.setAttribute("today", true);
                news = newsService.getAllToday();
            } else {
                news = newsService.getAll();
            }
        }

        req.setAttribute("news", news);
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/news.html").forward(req, resp);
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
