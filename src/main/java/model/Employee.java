package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Employee implements Comparable<Employee> {
    private final String id;
    private final String name;
    private final EmployeeType type;
    private Integer callsTaken;

    private Boolean available;

    private static final Logger logger = LoggerFactory.getLogger("employee");

    public Employee(String id, String name, EmployeeType type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = Boolean.TRUE;
        this.callsTaken = 0;
    }

    // Set call status to in progress -> wait for call to end -> if ok set status to finshed   -> set employee to available and add a call taken
    //                                                        -> if error set status to missed
    public void takeCall(Call c) {
        logger.debug("Employee {} of type {} is going to take call {}", Arrays.asList(this.id, this.type, c.getId()));
        c.setStatus(CallStatus.IN_PROGRESS);
        try {
            Thread.sleep(c.getDuration());
            c.setStatus(CallStatus.FINISHED);
        } catch (InterruptedException e) {
            c.setStatus(CallStatus.MISSED);
            logger.info("Problem while taking call " + c.getId());
        } finally {
            this.callsTaken++;
            this.available = Boolean.TRUE;
        }
    }

    public int compareTo(Employee e) {
        return this.type.getOrder() - e.type.getOrder();
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getCallsTaken() {
        return callsTaken;
    }

    public EmployeeType getType() {
        return type;
    }
}
