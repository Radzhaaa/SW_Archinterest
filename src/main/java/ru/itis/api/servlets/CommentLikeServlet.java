package ru.itis.api.servlets;

import ru.itis.logic.services.CommentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CommentLikeServlet", value = "/comment/like/*")
public class CommentLikeServlet extends HttpServlet {

    private CommentService commentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.commentService = (CommentService) config.getServletContext().getAttribute("commentService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String referer = req.getHeader("Referer");
        Long commentId = Long.parseLong(req.getPathInfo().replace("/", ""));
        commentService.like(commentId);
        resp.sendRedirect(referer);
    }
}
