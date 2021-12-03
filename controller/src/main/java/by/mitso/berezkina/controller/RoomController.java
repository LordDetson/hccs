package by.mitso.berezkina.controller;

import java.io.IOException;
import java.util.Map;

import org.hibernate.SessionFactory;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.factor.Factory;
import by.mitso.berezkina.factor.RoomTypeFactory;
import by.mitso.berezkina.field.FieldUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoomController", urlPatterns = { "/room/type/add" })
public class RoomController extends HttpServlet {

    private static final String ROOM_TYPE_ADD = "/room/type/add";
    private static final String ROOM_TYPE_ADD_VIEW = "/room/createRoomType.jsp";

    private CrudRepository<RoomType, Integer> roomTypeRepository;
    private Factory<RoomType> roomTypeFactory = RoomTypeFactory.getInstance();

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("SessionFactory");
        roomTypeRepository = new CrudRepositoryImpl<>(sessionFactory.openSession(), RoomType.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isRoomTypeAdd(req)) {
            req.setAttribute("fields", FieldUtil.getRoomTypeOrderedFormFields());
            forwardRoomTypeForm(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isRoomTypeAdd(req)) {
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(RoomType.getAllFields(), req);
            RoomType roomType = roomTypeFactory.factor(fieldValueMap);
            roomTypeRepository.save(roomType);
        }
    }

    private boolean isRoomTypeAdd(HttpServletRequest req) {
        return isAction(req, ROOM_TYPE_ADD);
    }

    private boolean isAction(HttpServletRequest req, String action) {
        return req.getRequestURI().contains(action);
    }

    private void forwardRoomTypeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(ROOM_TYPE_ADD_VIEW).forward(req, resp);
    }
}
