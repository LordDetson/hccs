package by.mitso.berezkina.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import by.mitso.berezkina.factor.Factory;
import by.mitso.berezkina.factor.RoomTypeFactory;
import by.mitso.berezkina.field.FieldUtil;
import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.form.InputForm;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.TableModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoomController", urlPatterns = { "/rooms/types", "/room/type/add", "/room/type/edit", "/room/type/delete" })
public class RoomController extends HttpServlet {

    private static final String ADD_ROOM_TYPE = "/room/type/add";
    private static final String GET_ROOM_TYPES = "/rooms/types";
    private static final String EDIT_ROOM_TYPE = "/room/type/edit";
    private static final String DELETE_ROOM_TYPE = "/room/type/delete";
    private static final String STANDARD_FORM_VIEW = "/template/standardForm.jsp";
    private static final String STANDARD_TABLE_VIEW = "/template/standardTable.jsp";

    private CrudRepository<RoomType, Integer> roomTypeRepository;
    private Factory<RoomType> roomTypeFactory = RoomTypeFactory.getInstance();

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = (SessionFactory) getServletContext().getAttribute("SessionFactory");
        roomTypeRepository = new CrudRepositoryImpl<>(sessionFactory.openSession(), RoomType.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isRoomTypeCreation(req)) {
            InputForm inputForm = new InputForm(
                    "Форма типа комнаты",
                    "createRoomType",
                    ADD_ROOM_TYPE,
                    FieldUtil.getRoomTypeOrderedInputFields(),
                    "Создать");
            req.setAttribute("inputForm", inputForm);
            forwardStandardForm(req, resp);
        }
        else if(isRoomTypeShow(req)) {
            TableModel<RoomType> tableModel = createRoomTypeTableModel();
            req.setAttribute("tableModel", tableModel);
            forwardStandardTable(req, resp);
        }
        else if(isRoomTypeEditing(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomTypeField.ID.getName()));
            Optional<RoomType> roomTypeToEdit = roomTypeRepository.findById(id);
            if(roomTypeToEdit.isPresent()) {
                Set<InputField> inputFields = FieldUtil.getRoomTypeOrderedInputFields();
                for(InputField inputField : inputFields) {
                    Object value = roomTypeToEdit.get().getFieldValue(inputField.getField());
                    if(value != null) {
                        inputField.setValue(value.toString());
                    }
                }
                InputForm inputForm = new InputForm(
                        "Форма типа комнаты",
                        "editRoomType",
                        EDIT_ROOM_TYPE + "?id=" + id,
                        inputFields,
                        "Обновить");
                req.setAttribute("inputForm", inputForm);
                forwardStandardForm(req, resp);
            }
        }
        else if(isAction(req, DELETE_ROOM_TYPE)) {
            Integer id = Integer.valueOf(req.getParameter(RoomTypeField.ID.getName()));
            roomTypeRepository.deleteById(id);
            resp.sendRedirect(GET_ROOM_TYPES);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isRoomTypeCreation(req)) {
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(RoomType.getAllFields(), req);
            RoomType roomType = roomTypeFactory.factor(fieldValueMap);
            roomTypeRepository.save(roomType);
            resp.sendRedirect(GET_ROOM_TYPES);
        }
        else if(isRoomTypeEditing(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomTypeField.ID.getName()));
            if(roomTypeRepository.existsById(id)) {
                Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(RoomType.getAllFields(), req);
                RoomType roomType = roomTypeFactory.factor(fieldValueMap);
                roomType.setId(id);
                roomTypeRepository.save(roomType);
                resp.sendRedirect(GET_ROOM_TYPES);
            }
        }
    }

    private boolean isRoomTypeCreation(HttpServletRequest req) {
        return isAction(req, ADD_ROOM_TYPE);
    }

    private boolean isRoomTypeShow(HttpServletRequest req) {
        return isAction(req, GET_ROOM_TYPES);
    }

    private boolean isRoomTypeEditing(HttpServletRequest req) {
        return isAction(req, EDIT_ROOM_TYPE);
    }

    private boolean isAction(HttpServletRequest req, String action) {
        return req.getRequestURI().contains(action);
    }

    private TableModel<RoomType> createRoomTypeTableModel() {
        String title = "Таблица типов комнат";
        Iterator<RoomType> iterator = roomTypeRepository.findAll().iterator();
        List<RoomType> roomTypes = new ArrayList<>();
        while(iterator.hasNext()) {
            roomTypes.add(iterator.next());
        }
        TableModel<RoomType> tableModel = new TableModel<>(title, roomTypes) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(RoomTypeField.ID).setCaption("#");
                list.add(RoomTypeField.NAME);
                list.add(RoomTypeField.DESCRIPTION);
                list.add(RoomTypeField.MIN_PEOPLE);
                list.add(RoomTypeField.MAX_PEOPLE);
                list.add(RoomTypeField.MIN_BEDS);
                list.add(RoomTypeField.MAX_BEDS);
                return list;
            }
        };
        tableModel.setEditAction(EDIT_ROOM_TYPE);
        tableModel.setDeleteAction(DELETE_ROOM_TYPE);
        return tableModel;
    }

    private void forwardStandardForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(STANDARD_FORM_VIEW).forward(req, resp);
    }

    private void forwardStandardTable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher(STANDARD_TABLE_VIEW).forward(req, resp);
    }
}
