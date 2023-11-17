package ru.itis.api.servlets;

import ru.itis.dao.entities.Image;
import ru.itis.dao.entities.User;
import ru.itis.logic.services.ImageService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GalleryServlet", value = "/gallery")
@MultipartConfig
public class GalleryServlet extends HttpServlet {

    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        this.imageService = (ImageService) servletContext.getAttribute("imageService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        HttpSession session = req.getSession();

        List<Image> images = imageService.findAll();

        User current = (User) session.getAttribute("current");
        if (current != null) {
            req.setAttribute("current", current);
        }

        req.setAttribute("images", images);
        req.getServletContext().getRequestDispatcher("/WEB-INF/templates/gallery.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part file = req.getPart("image");
        imageService.create(file);
        resp.sendRedirect("/archinterest/gallery");
    }
}
