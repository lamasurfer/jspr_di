package org.example.servlet;


import org.example.controller.PostController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    public static final String VALID_PATH = "/api/posts";
    public static final String VALID_ID = "/\\d+";

    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("org.example");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isValidPath(req)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if (hasId(req)) {
            final var id = getId(req);
            controller.getById(id, resp);
        } else {
            controller.all(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isValidPath(req)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            controller.save(req.getReader(), resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!hasId(req)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            final var id = getId(req);
            controller.removeById(id, resp);
        }
    }

    public boolean isValidPath(HttpServletRequest req) {
        return req.getRequestURI().matches(VALID_PATH);
    }

    public boolean hasId(HttpServletRequest req) {
        return req.getRequestURI().matches(VALID_PATH + VALID_ID);
    }

    public long getId(HttpServletRequest req) {
        var path = req.getRequestURI();
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

