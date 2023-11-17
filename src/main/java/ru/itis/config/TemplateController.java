package ru.itis.config;


import freemarker.ext.servlet.FreemarkerServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(
        urlPatterns = {"*.html", "*.ftl", "*.ftlh"}, // need to define the type of page suffix that Freemarker parses
        loadOnStartup = 0,
        name = "templateController",
        displayName = "TemplateController",
        initParams = {
                @WebInitParam(name = "TemplatePath", value = "/"),
                @WebInitParam(name = "NoCache", value = "true"),
                @WebInitParam(name = "ContentType", value = "text/html; charset=UTF-8"),
                @WebInitParam(name = "template_update_delay", value = "0"),
                @WebInitParam(name = "default_encoding", value = "UTF-8"),
                @WebInitParam(name = "number_format", value = "0.##########")
        }
)
public class TemplateController extends FreemarkerServlet {
    private static final long serialVersionUID = 8714019900490761087L;
}

