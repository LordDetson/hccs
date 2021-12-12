package by.mitso.berezkina.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignmentStatus;
import by.mitso.berezkina.domain.RoomType;

public class RoomRepository extends CrudRepositoryImpl<Room, Integer> {

    private static final List<RoomAssignmentStatus> notFreeStatuses = List.of(RoomAssignmentStatus.OVERDUE, RoomAssignmentStatus.BOOKED,
            RoomAssignmentStatus.IN_PROGRESS);

    public RoomRepository(EntityManager entityManager) {
        super(entityManager, Room.class);
    }

    public List<Room> find(LocalDate startDate, LocalDate completeDate, RoomType roomType,
            Byte numberOfBeds, Byte additionalPersons) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(completeDate);
        byte countOfPeople = 1;
        if(additionalPersons != null) {
            countOfPeople += additionalPersons;
        }
        List<Room> result = new ArrayList<>();
        Iterable<Room> all = findAll();
        for(Room room : all) {
            boolean isFree = room.getAssignments().stream()
                    .filter(assignment -> notFreeStatuses.contains(assignment.getStatus()))
                    .noneMatch(assignment -> {
                        LocalDate assignStartDate = assignment.getStartDate();
                        LocalDate assignCompleteDate = assignment.getCompleteDate();
                        return startDate.isEqual(assignStartDate) ||
                                startDate.isEqual(assignCompleteDate) ||
                                completeDate.isEqual(assignStartDate) ||
                                completeDate.isEqual(assignCompleteDate) ||
                                (startDate.isAfter(assignStartDate) && startDate.isBefore(assignCompleteDate)) ||
                                (completeDate.isBefore(assignStartDate) && completeDate.isAfter(assignCompleteDate)) ||
                                (startDate.isBefore(assignStartDate) && startDate.isBefore(assignCompleteDate) && completeDate.isAfter(assignStartDate) && completeDate.isBefore(assignCompleteDate)) ||
                                (startDate.isAfter(assignStartDate) && startDate.isBefore(assignCompleteDate) && completeDate.isAfter(assignStartDate) && completeDate.isAfter(assignCompleteDate));
                    });
            boolean equalRoomType = roomType == null || room.getRoomType().equals(roomType);
            boolean equalNumberOfBeds = numberOfBeds == null || room.getNumberOfBeds().compareTo(numberOfBeds) == 0;
            boolean betweenMinMaxPeople = room.getMinPeople().compareTo(countOfPeople) <= 0 &&
                            room.getMaxPeople().compareTo(countOfPeople) >= 0;
            if(isFree && equalRoomType && equalNumberOfBeds && betweenMinMaxPeople) {
                result.add(room);
            }
        }
        return result;
    }
}
