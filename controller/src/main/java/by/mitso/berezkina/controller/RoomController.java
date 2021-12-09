package by.mitso.berezkina.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.SessionFactory;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import by.mitso.berezkina.factor.Factory;
import by.mitso.berezkina.factor.RoomFactory;
import by.mitso.berezkina.factor.RoomTypeFactory;
import by.mitso.berezkina.field.DynamicField;
import by.mitso.berezkina.field.FieldUtil;
import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.form.FormSubmitButton;
import by.mitso.berezkina.form.InputFormModel;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.CrudTableModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RoomController", urlPatterns = { "/rooms/*", "/room/*" })
public class RoomController extends BaseController {

    private static final String ADD_ROOM_TYPE = "/room/type/add";
    private static final String GET_ROOM_TYPES = "/rooms/types";
    private static final String EDIT_ROOM_TYPE = "/room/type/edit";
    private static final String DELETE_ROOM_TYPE = "/room/type/delete";

    private static final String ADD_ROOM = "/room/add";
    private static final String ADDITIONAL_ROOM_PARAMS_PARAMETER = "additionalRoomParams";
    private static final String GET_ROOMS = "/rooms";
    private static final String EDIT_ROOM = "/room/edit";
    private static final String EDIT_ADDITIONAL_ROOM_PARAMS = "/room/edit/additional-params";
    private static final String DELETE_ROOM = "/room/delete";

    private static final Field ROOM_TYPES_FIELD = new DynamicField(RoomField.ROOM_TYPE.getName(), "типы комнат", Set.class, true, null, null);

    private CrudRepository<RoomType, Integer> roomTypeRepository;
    private CrudRepository<Room, Integer> roomRepository;
    private Factory<RoomType> roomTypeFactory = RoomTypeFactory.getInstance();
    private Factory<Room> roomFactory;

    @Override
    public void init() throws ServletException {
        SessionFactory sessionFactory = getSessionFactory();
        roomTypeRepository = new CrudRepositoryImpl<>(sessionFactory.openSession(), RoomType.class);
        roomRepository = new CrudRepositoryImpl<>(sessionFactory.openSession(), Room.class);
        roomFactory = RoomFactory.getInstance(roomTypeRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isRoomTypeCreation(req)) {
            InputFormModel inputFormModel = new InputFormModel(
                    "Форма типа комнаты",
                    "createRoomType",
                    ADD_ROOM_TYPE,
                    FieldUtil.getRoomTypeOrderedInputFields(),
                    "Создать");
            forwardStandardForm(req, resp, inputFormModel);
        }
        else if(isRoomTypeShow(req)) {
            CrudTableModel<RoomType> tableModel = createRoomTypeTableModel();
            forwardCrudTable(req, resp, tableModel);
        }
        else if(isRoomTypeEditing(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomTypeField.ID.getName()));
            Optional<RoomType> roomTypeToEdit = roomTypeRepository.findById(id);
            if(roomTypeToEdit.isPresent()) {
                Set<InputField> inputFields = FieldUtil.getRoomTypeOrderedInputFields();
                for(InputField inputField : inputFields) {
                    Object value = roomTypeToEdit.get().getFieldValue(inputField.getField());
                    if(value != null) {
                        inputField.setValues(Collections.singleton(value.toString()));
                    }
                }
                InputFormModel inputFormModel = new InputFormModel(
                        "Форма типа комнаты",
                        "editRoomType",
                        EDIT_ROOM_TYPE + "?id=" + id,
                        inputFields,
                        "Обновить");
                forwardStandardForm(req, resp, inputFormModel);
            }
        }
        else if(isRoomTypeDeletion(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomTypeField.ID.getName()));
            roomTypeRepository.deleteById(id);
            resp.sendRedirect(GET_ROOM_TYPES);
        }
        else if(isRoomCreation(req)) {
            Set<InputField> inputFields = FieldUtil.getMainRoomOrderedInputFields();
            addRoomTypesField(inputFields);
            InputFormModel inputFormModel = new InputFormModel(
                    "Форма комнаты",
                    "createRoom",
                    ADD_ROOM,
                    inputFields,
                    "Создать");
            inputFormModel.getSubmitButtons()
                    .add(new FormSubmitButton("Доп. параметры", ADD_ROOM + "?" + ADDITIONAL_ROOM_PARAMS_PARAMETER));
            forwardStandardForm(req, resp, inputFormModel);
        }
        else if(isRoomShow(req)) {
            CrudTableModel<Room> tableModel = createRoomTableModel();
            forwardCrudTable(req, resp, tableModel);
        }
        else if(isRoomEditing(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomField.ID.getName()));
            Optional<Room> roomToEdit = roomRepository.findById(id);
            if(roomToEdit.isPresent()) {
                if(isAction(req, EDIT_ADDITIONAL_ROOM_PARAMS)) {
                    Set<InputField> inputFields = FieldUtil.getAllRoomOrderedInputFields();
                    inputFields.stream()
                            .filter(inputField -> inputField.getField().equals(RoomField.ROOM_TYPE))
                            .findFirst().ifPresent(inputField -> inputField.setReadonly(true));
                    for(InputField inputField : inputFields) {
                        Object value = roomToEdit.get().getFieldValue(inputField.getField());
                        if(value != null) {
                            if(inputField.getField() == RoomField.ROOM_TYPE) {
                                RoomType roomType = (RoomType) value;
                                inputField.setValues(Collections.singleton(roomType.getName()));
                            }
                            else {
                                inputField.setValues(Collections.singleton(value.toString()));
                            }
                        }
                    }
                    InputFormModel roomInputFormModel = new InputFormModel(
                            "Форма комнаты с дополнительными параметрами",
                            "editAdditionalRoomParams",
                            EDIT_ADDITIONAL_ROOM_PARAMS + "?id=" + id,
                            inputFields,
                            "Обновить");
                    forwardStandardForm(req, resp, roomInputFormModel);
                }
                else {
                    Set<InputField> inputFields = FieldUtil.getMainRoomOrderedInputFields();
                    for(InputField inputField : inputFields) {
                        Object value = roomToEdit.get().getFieldValue(inputField.getField());
                        if(value != null) {
                            inputField.setValues(Collections.singleton(value.toString()));
                        }
                    }
                    addRoomTypesField(inputFields, roomToEdit.get());
                    InputFormModel inputFormModel = new InputFormModel(
                            "Форма комнаты",
                            "editRoom",
                            EDIT_ROOM + "?id=" + id,
                            inputFields,
                            "Обновить");
                    inputFormModel.getSubmitButtons()
                            .add(new FormSubmitButton("Доп. параметры", EDIT_ROOM + "?id=" + id + "&" + ADDITIONAL_ROOM_PARAMS_PARAMETER));
                    forwardStandardForm(req, resp, inputFormModel);
                }
            }
        }
        else if(isRoomDeletion(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomField.ID.getName()));
            roomRepository.deleteById(id);
            resp.sendRedirect(GET_ROOMS);
        }
    }

    private void addRoomTypesField(Set<InputField> inputFields) {
        addRoomTypesField(inputFields, null);
    }

    private void addRoomTypesField(Set<InputField> inputFields, Room room) {
        InputField roomTypesInputField = FieldUtil.convertToInputField(ROOM_TYPES_FIELD);
        Iterator<RoomType> iterator = roomTypeRepository.findAll().iterator();
        Set<String> roomTypeNames = new LinkedHashSet<>();
        while(iterator.hasNext()) {
            roomTypeNames.add(iterator.next().getName());
        }
        roomTypesInputField.setValues(roomTypeNames);
        if(room != null) {
            roomTypesInputField.setSelectedValue(room.getRoomType().getName());
        }
        inputFields.add(roomTypesInputField);
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
        else if(isRoomCreation(req)) {
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Room.getAllFields(), req);
            Room room = roomFactory.factor(fieldValueMap);
            room = roomRepository.save(room);
            if(req.getParameterMap().containsKey(ADDITIONAL_ROOM_PARAMS_PARAMETER)) {
                resp.sendRedirect(EDIT_ADDITIONAL_ROOM_PARAMS + "?id=" + room.getId());
            }
            else {
                resp.sendRedirect(GET_ROOMS);
            }
        }
        else if(isRoomEditing(req)) {
            Integer id = Integer.valueOf(req.getParameter(RoomField.ID.getName()));
            Optional<Room> room = roomRepository.findById(id);
            if(room.isPresent()) {
                Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(Room.getAllFields(), req);
                String roomTypeName = (String) fieldValueMap.get(RoomField.ROOM_TYPE);
                roomTypeRepository.findByField(RoomTypeField.NAME, roomTypeName)
                        .ifPresent(type -> fieldValueMap.put(RoomField.ROOM_TYPE, type));
                room.get().setFieldValue(fieldValueMap);
                roomRepository.save(room.get());
                if(req.getParameterMap().containsKey(ADDITIONAL_ROOM_PARAMS_PARAMETER)) {
                    resp.sendRedirect(EDIT_ADDITIONAL_ROOM_PARAMS + "?id=" + id);
                }
                else {
                    resp.sendRedirect(GET_ROOMS);
                }
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

    private boolean isRoomTypeDeletion(HttpServletRequest req) {
        return isAction(req, DELETE_ROOM_TYPE);
    }

    private boolean isRoomCreation(HttpServletRequest req) {
        return isAction(req, ADD_ROOM);
    }

    private boolean isRoomShow(HttpServletRequest req) {
        return isAction(req, GET_ROOMS);
    }

    private boolean isRoomEditing(HttpServletRequest req) {
        return isAction(req, EDIT_ROOM);
    }

    private boolean isRoomDeletion(HttpServletRequest req) {
        return isAction(req, DELETE_ROOM);
    }

    private CrudTableModel<RoomType> createRoomTypeTableModel() {
        String title = "Таблица типов комнат";
        Iterator<RoomType> iterator = roomTypeRepository.findAll().iterator();
        List<RoomType> roomTypes = new ArrayList<>();
        while(iterator.hasNext()) {
            roomTypes.add(iterator.next());
        }
        CrudTableModel<RoomType> tableModel = new CrudTableModel<>(title, roomTypes) {

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
        tableModel.setCreateAction(ADD_ROOM_TYPE);
        tableModel.setEditAction(EDIT_ROOM_TYPE);
        tableModel.setDeleteAction(DELETE_ROOM_TYPE);
        return tableModel;
    }

    private CrudTableModel<Room> createRoomTableModel() {
        String title = "Таблица комнат";
        Iterator<Room> iterator = roomRepository.findAll().iterator();
        List<Room> rooms = new ArrayList<>();
        while(iterator.hasNext()) {
            rooms.add(iterator.next());
        }
        CrudTableModel<Room> crudTableModel = new CrudTableModel<>(title, rooms) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(RoomField.ID).setCaption("#");
                list.add(RoomField.NUMBER);
                list.add(RoomField.ROOM_TYPE);
                list.add(RoomField.DESCRIPTION);
                list.add(RoomField.MIN_PEOPLE);
                list.add(RoomField.MAX_PEOPLE);
                list.add(RoomField.NUMBER_OF_BEDS);
                return list;
            }

            @Override
            public Object getValueAt(Room element, Column column) {
                Field field = column.getField();
                if(field == RoomField.ROOM_TYPE) {
                    String name = element.getRoomType().getName();
                    return String.format("<a href=\"#\">%s</a>", name);
                }
                return super.getValueAt(element, column);
            }
        };
        crudTableModel.setCreateAction(ADD_ROOM);
        crudTableModel.setEditAction(EDIT_ROOM);
        crudTableModel.setDeleteAction(DELETE_ROOM);
        return crudTableModel;
    }
}
