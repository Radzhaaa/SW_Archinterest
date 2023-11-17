package ru.itis.api.servlets;

import ru.itis.logic.services.CommentService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CommentDislikeServlet", value = "/comment/dislike/*")
public class CommentDislikeServlet extends HttpServlet {

    private CommentService commentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.commentService = (CommentService) config.getServletContext().getAttribute("commentService");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String referer = req.getHeader("Referer");
        Long commentId = Long.parseLong(req.getPathInfo().replace("/", ""));
        commentService.dislike(commentId);
        resp.sendRedirect(referer);
    }
}
