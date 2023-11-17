package ru.itis.config.listeners;

import ru.itis.dao.repositories.*;
import ru.itis.dao.repositories.impl.*;
import ru.itis.dao.utils.JdbcUtil;
import ru.itis.logic.services.*;
import ru.itis.logic.services.impl.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

@WebListener
public class AppConfigServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        Connection connection = JdbcUtil.getConnection();

        CommentRepository commentRepository = new CommentRepositoryImpl(connection);
        ImageRepository imageRepository = new ImageRepositoryImpl(connection);
        NewsRepository newsRepository = new NewsRepositoryImpl(connection);
        ProjectRepository projectRepository = new ProjectRepositoryImpl(connection);
        TagRepository tagRepository = new TagRepositoryImpl(connection);
        UserRepository userRepository = new UserRepositoryImpl(connection);

        CommentService commentService = new CommentServiceImpl(commentRepository);
        ImageService imageService = new ImageServiceImpl(imageRepository);
        NewsService newsService = new NewsServiceImpl(newsRepository, tagRepository);
        ProjectService projectService = new ProjectServiceImpl(projectRepository);
        TagService tagService = new TagServiceImpl(tagRepository);
        UserService userService = new UserServiceImpl(userRepository);

        servletContext.setAttribute("commentService", commentService);
        servletContext.setAttribute("imageService", imageService);
        servletContext.setAttribute("newsService", newsService);
        servletContext.setAttribute("projectService", projectService);
        servletContext.setAttribute("tagService", tagService);
        servletContext.setAttribute("userService", userService);
    }
}
