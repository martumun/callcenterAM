package service;


import model.Call;
import model.Employee;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
    private List<Employee> employees = new ArrayList<>();
    private Queue<Call> waitingCallsQueue = new LinkedList<>();

    ExecutorService executor = Executors.newFixedThreadPool(10);

    // if employee is available it handles call otherwise call is put in a queue to avoid loosing them
    public void dispatchCall(Call call) {
        executor.execute(() -> {
            Optional<Employee> employeeOptional = selectAvailableEmployee();
            if (employeeOptional.isPresent()) {
                Employee e = employeeOptional.get();
                e.setAvailable(Boolean.FALSE);
                e.takeCall(call);
                if (!waitingCallsQueue.isEmpty()) e.takeCall(waitingCallsQueue.poll());
            } else {
                waitingCallsQueue.add(call);
            }
        });
    }

    // Waits for threads to end
    public void waitTermination() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(25L, TimeUnit.SECONDS);
    }

    // Sorts employee list and finds the first available
    private Optional<Employee> selectAvailableEmployee() {
        return employees.stream().filter(e -> e.isAvailable()).sorted().findFirst();
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

}
