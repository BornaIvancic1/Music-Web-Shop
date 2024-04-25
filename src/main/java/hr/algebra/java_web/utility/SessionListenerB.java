package hr.algebra.java_web.utility;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@WebListener
@Component
public class SessionListenerB implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session created with ID: " + se.getSession().getId());

        String ipAdress= ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        System.out.println("IP address: "+ipAdress);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session destroyed with ID: " + se.getSession().getId());

    }}