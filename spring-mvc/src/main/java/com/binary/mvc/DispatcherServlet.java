package com.binary.mvc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Binary
 */
@WebServlet(name = "DispatcherServlet", value = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static String ROOT_PATH = "controller";

    private static Map<String, Method> methodMap = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();
        path = path.substring(path.lastIndexOf("/") + 1);
        Method method = methodMap.get(path);
        if (method != null) {
            Class<?> declaringClass = method.getDeclaringClass();
            try {
                method.invoke(declaringClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init() throws ServletException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("controller");
        if (resource != null) {
            findClasses(new File(resource.getPath()));
        }
    }

    private static void findClasses(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File tmpFile : files) {
                findClasses(tmpFile);
            }
        } else {
            try {
                Class<?> clazz = Class.forName("controller." + file.getName().replaceAll(".class", ""));
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        String value = requestMapping.value();
                        methodMap.put(value, method);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
