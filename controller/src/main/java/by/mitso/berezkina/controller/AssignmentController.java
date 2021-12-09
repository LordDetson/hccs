package by.mitso.berezkina.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Session;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.table.Column;
import by.mitso.berezkina.table.ColumnList;
import by.mitso.berezkina.table.SelectionTableModel;
import by.mitso.berezkina.table.SelectionTableModel.SelectionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AssignmentController", urlPatterns = { "/customer/select", "/room/select", "/room/assignment/*",
        "/room/assignments/*" })
public class AssignmentController extends BaseController {

    private static final String SELECT_CUSTOMER = "/customer/select";
    private static final String SELECT_ROOM = "/room/select";
    private static final String ADD_ASSIGNMENT = "/room/assignment/add";
    private static final String GET_ASSIGNMENTS = "/room/assignments";
    private static final String EDIT_ASSIGNMENT = "/room/assignment/edit";
    private static final String DELETE_ASSIGNMENT = "/room/assignment/delete";

    private static final String SELECT_CUSTOMER_VIEW = "/view/customer/customerSelectionForm.jsp";
    private static final String SELECT_ROOM_VIEW = "/view/room/roomSelectionForm.jsp";

    private CrudRepository<RoomAssignment, Long> assignmentRepository;
    private CrudRepository<Room, Integer> roomRepository;
    private CrudRepository<Customer, Long> customerRepository;

    @Override
    public void init() throws ServletException {
        Session session = getSessionFactory().openSession();
        assignmentRepository = new CrudRepositoryImpl<>(session, RoomAssignment.class);
        roomRepository = new CrudRepositoryImpl<>(session, Room.class);
        customerRepository = new CrudRepositoryImpl<>(session, Customer.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, SELECT_CUSTOMER)) {
            List<Customer> customers = new ArrayList<>();
            for(Customer customer : customerRepository.findAll()) {
                customers.add(customer);
            }
            SelectionTableModel<Customer> customerTableModel = createCustomerSelectionTableModel(customers);
            req.setAttribute("selectionTableModel", customerTableModel);
            getServletContext().getRequestDispatcher(SELECT_CUSTOMER_VIEW).forward(req, resp);
        }
        else if(isAction(req, SELECT_ROOM)) {
            List<Room> rooms = new ArrayList<>();
            for(Room room : roomRepository.findAll()) {
                rooms.add(room);
            }
            SelectionTableModel<Room> roomTableModel = createRoomSelectionTableModel(rooms);
            req.setAttribute("selectionTableModel", roomTableModel);
            getServletContext().getRequestDispatcher(SELECT_ROOM_VIEW).forward(req, resp);
        }
        else if(isAction(req, ADD_ASSIGNMENT)) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(isAction(req, SELECT_ROOM)) {
            Long id = Long.valueOf(req.getParameter("selections"));
            Optional<Customer> selectedCustomer = customerRepository.findById(id);
            if(selectedCustomer.isPresent()) {
                req.getSession().setAttribute("selectedCustomer", selectedCustomer.get());
                resp.sendRedirect(SELECT_ROOM);
            }
        }
        else if(isAction(req, ADD_ASSIGNMENT)) {
            List<Integer> ids = Arrays.stream(req.getParameterValues("selections"))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
            Customer owner = (Customer) req.getSession().getAttribute("selectedCustomer");
            List<RoomAssignment> assignments = new ArrayList<>();
            for(Room room : roomRepository.findAllById(ids)) {
                LocalDate now = LocalDate.now();
                assignments.add(new RoomAssignment(owner, room, now, now.plusDays(10)));
            }
            assignmentRepository.saveAll(assignments);
            req.getSession().removeAttribute("selectedCustomer");
        }
    }

    private SelectionTableModel<Customer> createCustomerSelectionTableModel(List<Customer> customers) {
        return new SelectionTableModel<>("Выбор клиента", customers, "selectionCustomer",
                SELECT_ROOM, SelectionType.RADIO) {

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

    private SelectionTableModel<Room> createRoomSelectionTableModel(List<Room> rooms) {
        return new SelectionTableModel<>("Выбор комнаты", rooms, "selectionRoom", ADD_ASSIGNMENT,
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
}
