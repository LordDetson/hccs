package by.mitso.berezkina.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import by.mitso.berezkina.application.RoomRepository;
import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.RoomAssignment.RoomAssignmentField;
import by.mitso.berezkina.domain.RoomAssignmentStatus;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import by.mitso.berezkina.field.DynamicField;
import by.mitso.berezkina.field.FieldUtil;
import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.form.InputFormModel;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.CrudTableModel;
import by.mitso.berezkina.table.SelectionTableModel;
import by.mitso.berezkina.table.SelectionTableModel.SelectionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AssignmentController", urlPatterns = { "/customer/search", "/customer/select", "/room/search", "/room/select", "/room/assignment/*",
        "/room/assignments/*" })
public class AssignmentController extends BaseController {

    private static final String SEARCH_CUSTOMER = "/customer/search";
    private static final String SELECT_CUSTOMER = "/customer/select";
    private static final String SEARCH_ROOM = "/room/search";
    public static final String SELECT_ROOM = "/room/select";
    private static final String ADD_ASSIGNMENT = "/room/assignment/add";
    private static final String GET_ASSIGNMENTS = "/room/assignments";
    private static final String EDIT_ASSIGNMENT = "/room/assignment/edit";

    private static final String SELECT_CUSTOMER_VIEW = "/view/customer/customerSelectionForm.jsp";
    private static final String SELECT_ROOM_VIEW = "/view/room/roomSelectionForm.jsp";

    public static final String SELECTED_CUSTOMER_ATTRIBUTE = "selectedCustomer";
    private static final String START_DATE_ATTRIBUTE = "startDate";
    private static final String COMPLETE_DATE_ATTRIBUTE = "completeDate";
    private static final String ADDITIONAL_PERSONS_ATTRIBUTE = "additionalPersons";

    private static final Field STATUSES_FIELD = new DynamicField(RoomAssignmentField.STATUS.getName(),
            RoomAssignmentField.STATUS.getCaption(), Set.class, true, null, null);
    private static final Field ROOM_TYPES_FIELD = new DynamicField(RoomField.ROOM_TYPE.getName(),
            "типы комнат", Set.class, false, null, null);

    private CrudRepository<RoomAssignment, Long> assignmentRepository;
    private RoomRepository roomRepository;
    private CrudRepository<RoomType, Integer> roomTypeRepository;
    private CrudRepository<Customer, Long> customerRepository;

    @Override
    public void init() throws ServletException {
        Session session = getSessionFactory().openSession();
        assignmentRepository = new CrudRepositoryImpl<>(session, RoomAssignment.class);
        roomRepository = new RoomRepository(session);
        roomTypeRepository = new CrudRepositoryImpl<>(session, RoomType.class);
        customerRepository = new CrudRepositoryImpl<>(session, Customer.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, SELECT_CUSTOMER)) {
            List<Customer> customers = new ArrayList<>();
            for(Customer customer : customerRepository.findAll()) {
                customers.add(customer);
            }
            SelectionTableModel<Customer> customerTableModel = createCustomerSelectionTableModel(req, customers);
            req.setAttribute("selectionTableModel", customerTableModel);
            getServletContext().getRequestDispatcher(SELECT_CUSTOMER_VIEW).forward(req, resp);
        }
        else if(isAction(req, SELECT_ROOM)) {
            Set<Field> fields = new LinkedHashSet<>();
            fields.add(RoomAssignmentField.START_DATE);
            fields.add(RoomAssignmentField.COMPLETE_DATE);
            fields.add(RoomField.NUMBER_OF_BEDS);
            fields.add(RoomAssignmentField.ADDITIONAL_PERSONS);
            Set<InputField> inputFields = FieldUtil.convertToInputFields(fields);
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(fields, req);
            inputFields.forEach(inputField -> {
                Field field = inputField.getField();
                Object value = fieldValueMap.get(field);
                if(value == null) {
                    if(field == RoomAssignmentField.START_DATE) {
                        value = LocalDate.now();
                    }
                    else if(field == RoomAssignmentField.COMPLETE_DATE) {
                        value = LocalDate.now().plusDays(1);
                    }
                    else if(field == RoomAssignmentField.ADDITIONAL_PERSONS) {
                        value = 0;
                    }
                }
                inputField.setValue(value != null ? value.toString() : "");
            });
            InputField roomTypesField = new InputField(ROOM_TYPES_FIELD);
            Set<String> roomTypeNames = new LinkedHashSet<>();
            for(RoomType roomType : roomTypeRepository.findAll()) {
                roomTypeNames.add(roomType.getName());
            }
            roomTypesField.setValues(roomTypeNames);
            roomTypesField.setSelectedValue(req.getParameter(ROOM_TYPES_FIELD.getName()));
            inputFields.add(roomTypesField);
            InputFormModel inputFormModel = new InputFormModel(
                    "Форма поиск комнат",
                    "searchRoom",
                    req.getContextPath() + SEARCH_ROOM,
                    inputFields,
                    "Искать"
            );
            SelectionTableModel<Room> roomTableModel = createRoomSelectionTableModel(req, Collections.emptyList());
            req.setAttribute("inputFormModel", inputFormModel);
            req.setAttribute("selectionTableModel", roomTableModel);
            getServletContext().getRequestDispatcher(SELECT_ROOM_VIEW).forward(req, resp);
        }
        else if(isAction(req, GET_ASSIGNMENTS)) {
            List<RoomAssignment> assignments = new ArrayList<>();
            for(RoomAssignment assignment : assignmentRepository.findAll()) {
                assignments.add(assignment);
            }
            CrudTableModel<RoomAssignment> crudTableModel = createAssignmentTableModel(req, assignments);
            forwardCrudTable(req, resp, crudTableModel);
        }
        else if(isAction(req, EDIT_ASSIGNMENT)) {
            Long id = Long.valueOf(req.getParameter(RoomAssignmentField.ID.getName()));
            Optional<RoomAssignment> assignment = assignmentRepository.findById(id);
            if(assignment.isPresent()) {
                RoomAssignmentStatus status = assignment.get().getStatus();
                Set<InputField> inputFields = FieldUtil.getAssignmentOrderedInputFields();
                inputFields.forEach(inputField -> {
                    Field field = inputField.getField();
                    Object value = assignment.get().getFieldValue(field);
                    if(field == RoomAssignmentField.OWNER) {
                        value = assignment.get().getOwner().getFullName();
                    }
                    else if(field == RoomAssignmentField.ROOM) {
                        value = assignment.get().getRoom().getNumber();
                    }
                    inputField.setValue(value != null ? value.toString() : "");
                    inputField.setReadonly(true);
                    if(inputField.getField() == RoomAssignmentField.ADDITIONAL_PERSONS &&
                            status != RoomAssignmentStatus.CANCELED && status != RoomAssignmentStatus.COMPLETED) {
                        inputField.setReadonly(false);
                    }
                });
                InputField statusesField = new InputField(STATUSES_FIELD);
                if(status == RoomAssignmentStatus.IN_PROGRESS || status == RoomAssignmentStatus.OVERDUE) {
                    statusesField.setValues(Set.of(status.getCaption(), RoomAssignmentStatus.COMPLETED.getCaption()));
                }
                else if(status == RoomAssignmentStatus.BOOKED) {
                    statusesField.setValues(Set.of(status.getCaption(), RoomAssignmentStatus.CANCELED.getCaption()));
                }
                else {
                    statusesField.setValue(status.getCaption());
                    statusesField.setReadonly(true);
                }
                statusesField.setSelectedValue(status.getCaption());
                inputFields.add(statusesField);
                InputFormModel inputFormModel = new InputFormModel(
                        "Форма назначения",
                        "edit",
                        req.getContextPath() + EDIT_ASSIGNMENT + "?id=" + id,
                        inputFields,
                        "Обновить");
                forwardStandardForm(req, resp, inputFormModel);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, SEARCH_CUSTOMER)) {
            CustomerField identifierNumberField = CustomerField.IDENTIFIER_NUMBER;
            String identifierNumber = req.getParameter(identifierNumberField.getName());
            Optional<Customer> customer = customerRepository.findByField(identifierNumberField, identifierNumber);
            SelectionTableModel<Customer> customerTableModel = createCustomerSelectionTableModel(req,
                    customer.map(Collections::singletonList).orElse(Collections.emptyList()));
            req.setAttribute(identifierNumberField.getName(), identifierNumber);
            req.setAttribute("selectionTableModel", customerTableModel);
            getServletContext().getRequestDispatcher(SELECT_CUSTOMER_VIEW).forward(req, resp);
        }
        else if(isAction(req, SELECT_ROOM)) {
            Long id = Long.valueOf(req.getParameter("selections"));
            if(id != null) {
                Optional<Customer> selectedCustomer = customerRepository.findById(id);
                selectedCustomer.ifPresent(customer -> req.getSession().setAttribute(SELECTED_CUSTOMER_ATTRIBUTE, customer));
            }
            resp.sendRedirect(req.getContextPath() + SELECT_ROOM);
        }
        else if(isAction(req, SEARCH_ROOM)) {
            Set<Field> fields = new LinkedHashSet<>();
            fields.add(RoomAssignmentField.START_DATE);
            fields.add(RoomAssignmentField.COMPLETE_DATE);
            fields.add(RoomField.NUMBER_OF_BEDS);
            fields.add(RoomAssignmentField.ADDITIONAL_PERSONS);
            fields.add(ROOM_TYPES_FIELD);
            Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(fields, req);
            LocalDate startDate = (LocalDate) fieldValueMap.get(RoomAssignmentField.START_DATE);
            LocalDate completeDate = (LocalDate) fieldValueMap.get(RoomAssignmentField.COMPLETE_DATE);
            String roomTypeName = (String) fieldValueMap.get(ROOM_TYPES_FIELD);
            RoomType roomType = roomTypeRepository.findByField(RoomTypeField.NAME, roomTypeName).orElse(null);
            Byte numberOfBeds = (Byte) fieldValueMap.get(RoomField.NUMBER_OF_BEDS);
            Byte additionalPersons = (Byte) fieldValueMap.get(RoomAssignmentField.ADDITIONAL_PERSONS);
            List<Room> rooms = roomRepository.find(startDate, completeDate, roomType, numberOfBeds, additionalPersons);

            fields.remove(ROOM_TYPES_FIELD);
            Set<InputField> inputFields = FieldUtil.convertToInputFields(fields);
            inputFields.forEach(inputField -> {
                Object value = fieldValueMap.get(inputField.getField());
                inputField.setValue(value != null ? value.toString() : "");
            });
            InputField roomTypesField = new InputField(ROOM_TYPES_FIELD);
            Set<String> roomTypeNames = new LinkedHashSet<>();
            for(RoomType roomType1 : roomTypeRepository.findAll()) {
                roomTypeNames.add(roomType1.getName());
            }
            roomTypesField.setValues(roomTypeNames);
            roomTypesField.setSelectedValue(req.getParameter(ROOM_TYPES_FIELD.getName()));
            inputFields.add(roomTypesField);
            InputFormModel inputFormModel = new InputFormModel(
                    "Форма поиск комнат",
                    "searchRoom",
                    req.getContextPath() + SEARCH_ROOM,
                    inputFields,
                    "Искать"
            );

            SelectionTableModel<Room> roomTableModel = createRoomSelectionTableModel(req, rooms);
            HttpSession session = req.getSession();
            session.setAttribute(START_DATE_ATTRIBUTE, startDate);
            session.setAttribute(COMPLETE_DATE_ATTRIBUTE, completeDate);
            session.setAttribute(ADDITIONAL_PERSONS_ATTRIBUTE, additionalPersons);
            req.setAttribute("inputFormModel", inputFormModel);
            req.setAttribute("selectionTableModel", roomTableModel);
            getServletContext().getRequestDispatcher(SELECT_ROOM_VIEW).forward(req, resp);
        }
        else if(isAction(req, ADD_ASSIGNMENT)) {
            List<Integer> ids = Arrays.stream(req.getParameterValues("selections"))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
            HttpSession session = req.getSession();
            Customer owner = (Customer) session.getAttribute(SELECTED_CUSTOMER_ATTRIBUTE);
            LocalDate startDate = (LocalDate) session.getAttribute(START_DATE_ATTRIBUTE);
            LocalDate completeDate = (LocalDate) session.getAttribute(COMPLETE_DATE_ATTRIBUTE);
            Byte additionalPersons = (Byte) session.getAttribute(ADDITIONAL_PERSONS_ATTRIBUTE);
            List<RoomAssignment> assignments = new ArrayList<>();
            for(Room room : roomRepository.findAllById(ids)) {
                RoomAssignment assignment = new RoomAssignment(owner, room, startDate, completeDate);
                assignment.setAdditionalPersons(additionalPersons);
                assignments.add(assignment);
            }
            assignmentRepository.saveAll(assignments);
            session.removeAttribute(SELECTED_CUSTOMER_ATTRIBUTE);
            session.removeAttribute(START_DATE_ATTRIBUTE);
            session.removeAttribute(COMPLETE_DATE_ATTRIBUTE);
            session.removeAttribute(ADDITIONAL_PERSONS_ATTRIBUTE);
            resp.sendRedirect(req.getContextPath() + GET_ASSIGNMENTS);
        }
        else if(isAction(req, EDIT_ASSIGNMENT)) {
            Long id = Long.valueOf(req.getParameter(RoomAssignmentField.ID.getName()));
            Optional<RoomAssignment> assignment = assignmentRepository.findById(id);
            if(assignment.isPresent()) {
                Map<Field, Object> fieldValueMap = FieldUtil.createFieldValueMap(RoomAssignment.getAllFields(), req);
                fieldValueMap.remove(RoomAssignmentField.OWNER);
                fieldValueMap.remove(RoomAssignmentField.ROOM);
                fieldValueMap.remove(RoomAssignmentField.START_DATE);
                fieldValueMap.remove(RoomAssignmentField.COMPLETE_DATE);
                if(fieldValueMap.containsKey(RoomAssignmentField.STATUS)) {
                    String statusCaption = (String) fieldValueMap.get(RoomAssignmentField.STATUS);
                    Arrays.stream(RoomAssignmentStatus.values())
                            .filter(status -> status.getCaption().equals(statusCaption))
                            .findFirst().ifPresent(status -> fieldValueMap.put(RoomAssignmentField.STATUS, status));
                }
                assignment.get().setFieldValue(fieldValueMap);
                assignmentRepository.save(assignment.get());
                resp.sendRedirect(req.getContextPath() + GET_ASSIGNMENTS);
            }
        }
    }

    private SelectionTableModel<Customer> createCustomerSelectionTableModel(HttpServletRequest req,
            List<Customer> customers) {
        return new SelectionTableModel<>("Выбор клиента", customers, "selectionCustomer",
                req.getContextPath() + SELECT_ROOM, SelectionType.RADIO) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(CustomerField.ID);
                list.add(CustomerField.FIRST_NAME);
                list.add(CustomerField.LAST_NAME);
                list.add(CustomerField.PASSPORT_ID);
                list.add(CustomerField.IDENTIFIER_NUMBER);
                list.add(CustomerField.COUNTRY);
                list.add(CustomerField.NATIONALITY);
                list.add(CustomerField.BIRTHDAY);
                list.add(CustomerField.GENDER);
                return list;
            }

            @Override
            public Object getValueAt(Customer customer, Column column) {
                Field field = column.getField();
                if(field == CustomerField.GENDER) {
                    return customer.getGender().getShortName();
                }
                return super.getValueAt(customer, column);
            }
        };
    }

    private SelectionTableModel<Room> createRoomSelectionTableModel(HttpServletRequest req, List<Room> rooms) {
        return new SelectionTableModel<>("Выбор комнаты", rooms, "selectionRoom", req.getContextPath() + ADD_ASSIGNMENT,
                SelectionType.CHECKBOX) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(RoomField.ID);
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
    }

    private CrudTableModel<RoomAssignment> createAssignmentTableModel(HttpServletRequest req,
            List<RoomAssignment> assignments) {
        CrudTableModel<RoomAssignment> crudTableModel = new CrudTableModel<RoomAssignment>("Таблица назначений", assignments) {

            @Override
            protected ColumnList createColumnList() {
                ColumnList list = new ColumnList();
                list.add(RoomAssignmentField.ID).setCaption("#");
                list.add(RoomAssignmentField.OWNER);
                list.add(RoomAssignmentField.ROOM);
                list.add(RoomAssignmentField.ADDITIONAL_PERSONS);
                list.add(RoomAssignmentField.START_DATE);
                list.add(RoomAssignmentField.COMPLETE_DATE);
                list.add(RoomAssignmentField.STATUS);
                return list;
            }

            @Override
            public Object getValueAt(RoomAssignment assignment, Column column) {
                Field field = column.getField();
                if(field == RoomAssignmentField.OWNER) {
                    return assignment.getOwner().getFullName();
                }
                else if(field == RoomAssignmentField.ROOM) {
                    return assignment.getRoom().getNumber();
                }
                else if(field == RoomAssignmentField.STATUS) {
                    return StringUtils.capitalize(assignment.getStatus().getCaption());
                }
                return super.getValueAt(assignment, column);
            }
        };
        crudTableModel.setCreateAction(req.getContextPath() + SELECT_CUSTOMER);
        crudTableModel.setEditAction(req.getContextPath() + EDIT_ASSIGNMENT);
        crudTableModel.setCanEdit(assignment -> assignment.getStatus() != RoomAssignmentStatus.COMPLETED &&
                assignment.getStatus() != RoomAssignmentStatus.CANCELED);
        return crudTableModel;
    }
}
