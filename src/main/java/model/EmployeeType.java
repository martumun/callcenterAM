package model;

public enum EmployeeType {
    DIRECTOR(2), SUPERVISOR(1), OPERATOR(0);

    private final int order;

    // Set an order value to order employee list with comparable
    EmployeeType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
