package ru.itis.api.filters;

import ru.itis.dao.entities.User;
import ru.itis.logic.services.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private UserService userService;
    private final List<String> allowedPaths = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
        allowedPaths.add("styles");
        allowedPaths.add("scripts");
        allowedPaths.add("images");
        allowedPaths.add("arch");
        allowedPaths.add("gallery");
        allowedPaths.add("reg");
        allowedPaths.add("auth");
        allowedPaths.add("logout");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie[] cookies = req.getCookies();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("current");

        String[] strings = req.getServletPath().split("/");

        User userByToken = userByToken(cookies);

        if (strings.length == 1 || allowedPaths.contains(strings[1])) {
            if (!strings[1].equals("logout") && (userByToken != null || user != null)) {
                session.setAttribute("current", user == null ? userByToken : user);
            }
            chain.doFilter(req, resp);
        } else {

            if (user != null) {
                chain.doFilter(req, resp);
                return;
            }

            if (userByToken != null) {
                session.setAttribute("current", userByToken);
                chain.doFilter(req, resp);
                return;
            }

            req.getRequestDispatcher("/archinterest/auth");
        }
    }

    private User userByToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String[] split = cookie.getValue().split("_");
                    String username = split[0];
                    String hash = split[1];
                    return userService.get(username, hash);
                }
            }
        }
        return null;
    }
}
