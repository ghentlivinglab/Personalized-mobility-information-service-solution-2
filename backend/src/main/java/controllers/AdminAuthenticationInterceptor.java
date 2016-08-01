package controllers;

import database_v2.controlLayer.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.exceptions.ExpiredException;
import models.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Class to intercept the /admin requests
 */
@CrossOrigin
public class AdminAuthenticationInterceptor extends HandlerInterceptorAdapter{
    
    private final Database database;
    
    /**
     * 
     * @param database Interface to communicate with the database.
     */
    public AdminAuthenticationInterceptor(Database database) {
        super();
        this.database = database;
    }
    
    /**
     * Method to handle an incoming request
     * @param request
     * @param response
     * @param handler
     * @return A boolean to indicate if a user has authorization or not
     * @throws Exception
     */
    @Override
    public boolean preHandle (HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String accessToken = (String) request.getHeader("Authorization");
        if (accessToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        
        String userId = accessToken.split("&t=")[0];
        
        User user = database.getUser(Integer.parseInt(userId));
        try {
            if(!TokenController.validateToken(accessToken, user.getEmailAsString())) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        } catch (ExpiredException ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        
        if (!user.isAdmin()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        
        return true;
    }
}
