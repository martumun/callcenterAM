import model.Call;
import model.CallStatus;
import model.Employee;
import model.EmployeeType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import service.Dispatcher;

import java.util.*;


public class DispatcherTest {

    private Dispatcher dispatcher = new Dispatcher();

    private List<Employee> employees;

    @Before
    public void employeesForTest() {
        employees = Arrays.asList(
                new Employee(UUID.randomUUID().toString(), "Cristian Pavon", EmployeeType.OPERATOR),
                new Employee(UUID.randomUUID().toString(), "Dario Benedetto", EmployeeType.OPERATOR),
                new Employee(UUID.randomUUID().toString(), "Jorge Rodriguez", EmployeeType.DIRECTOR),
                new Employee(UUID.randomUUID().toString(), "Martin Muñoz", EmployeeType.SUPERVISOR),
                new Employee(UUID.randomUUID().toString(), "Pablo Perez", EmployeeType.OPERATOR),
                new Employee(UUID.randomUUID().toString(), "Martin Palermo", EmployeeType.OPERATOR),
                new Employee(UUID.randomUUID().toString(), "Rolando Schiavi", EmployeeType.OPERATOR),
                new Employee(UUID.randomUUID().toString(), "Fernando Gago", EmployeeType.OPERATOR));
        dispatcher.setEmployees(employees);
    }


    @Test
    public void tenCallsTest() throws InterruptedException {
        List<Call> calls = generateCalls(10);
        calls.forEach(dispatcher::dispatchCall);
        dispatcher.waitTermination();
        calls.forEach(c -> Assert.assertTrue("Call " + c.getId() + " didnt finished", c.getStatus().equals(CallStatus.FINISHED)));
    }

    // Test more calls than threads that can be handled at the same time
    @Test
    public void moreCallsThanThreadsTest() throws InterruptedException {
        List<Call> calls = generateCalls(15);
        calls.forEach(dispatcher::dispatchCall);
        dispatcher.waitTermination();
        calls.forEach(c -> Assert.assertTrue("Call " + c.getId() + " didnt finished", c.getStatus().equals(CallStatus.FINISHED)));
    }

    // Only operator should take calls since there are only 3 calls (testing employees sort)
    @Test
    public void employeesCallPriorityTest() throws InterruptedException {
        List<Call> calls = generateCalls(3);
        calls.forEach(dispatcher::dispatchCall);
        dispatcher.waitTermination();
        Assert.assertTrue("A supervisor or a director handled a call",
                employees.stream()
                        .filter(e -> !e.getType().equals(EmployeeType.OPERATOR))
                        .filter(e -> e.getCallsTaken() > 0).count() == 0);
    }


    private List<Call> generateCalls(Integer callsQty) {
        if (callsQty == null || callsQty == 0) return Collections.emptyList();

        List<Call> calls = new ArrayList<>();

        for (int i = 1; i <= callsQty; i++) {
            calls.add(new Call(UUID.randomUUID().toString()));
        }

        return calls;
    }
}
