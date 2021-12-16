package by.mitso.berezkina.filter;

import static by.mitso.berezkina.controller.RoomController.*;

import java.io.IOException;
import java.util.List;

import by.mitso.berezkina.access.AccessChecker;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    private static final List<String> forAdministrationEndpoints = List.of(
            ADD_ROOM_TYPE,
            EDIT_ROOM_TYPE,
            DELETE_ROOM_TYPE,
            ADD_ROOM,
            EDIT_ROOM,
            DELETE_ROOM
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if(req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            boolean isAdministrator = AccessChecker.isAdministrator(httpRequest);
            if(!isAdministrator && forAdministrationEndpoints.stream().anyMatch(endpoint -> isAction(httpRequest, endpoint))) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    public boolean isAction(HttpServletRequest req, String action) {
        return req.getRequestURI().contains(action);
    }
}
