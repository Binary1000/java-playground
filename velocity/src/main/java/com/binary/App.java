package com.binary;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {
        Properties properties = new Properties();

        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");

        Velocity.init(properties);

        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("name", "Binary");

        Template template = Velocity.getTemplate("template.vm");

        StringWriter sw = new StringWriter();

        template.merge( velocityContext, sw );

        System.out.println(sw);
    }

}
