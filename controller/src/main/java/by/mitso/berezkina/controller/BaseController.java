package by.mitso.berezkina.controller;

import java.io.IOException;

import org.hibernate.SessionFactory;

import by.mitso.berezkina.form.InputForm;
import by.mitso.berezkina.listener.HibernateSessionFactoryListener;
import by.mitso.berezkina.table.TableModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BaseController extends HttpServlet {

    private static final String STANDARD_FORM_VIEW = "/template/standardForm.jsp";
    private static final String STANDARD_TABLE_VIEW = "/template/standardTable.jsp";

    public boolean isAction(HttpServletRequest req, String action) {
        return req.getRequestURI().contains(action);
    }

    public void forwardStandardForm(HttpServletRequest req, HttpServletResponse resp, InputForm inputForm) throws ServletException, IOException {
        req.setAttribute("inputForm", inputForm);
        getServletContext().getRequestDispatcher(STANDARD_FORM_VIEW).forward(req, resp);
    }

    public void forwardStandardTable(HttpServletRequest req, HttpServletResponse resp, TableModel<?> tableModel) throws ServletException, IOException {
        req.setAttribute("tableModel", tableModel);
        getServletContext().getRequestDispatcher(STANDARD_TABLE_VIEW).forward(req, resp);
    }

    public SessionFactory getSessionFactory() {
        return (SessionFactory) getServletContext().getAttribute(HibernateSessionFactoryListener.SESSION_FACTORY_ATTRIBUTE);
    }
}
