package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.Stack;

public class Order {
    private OrderState state;
    private Stack<OrderState> history;

    public Order(OrderState initialState) {
        this.state = initialState;
        this.history = new Stack<>();
    }

    public void nextState() throws OrderIsAlreadyFinalException {
        if (state == OrderState.DELIVERED || state == OrderState.CANCELED) {
            throw new OrderIsAlreadyFinalException();
        }
        history.push(state);
        switch (state) {
            case PLACED -> state = OrderState.PROCESSED;
            case PROCESSED -> state = OrderState.SHIPPED;
            case SHIPPED -> state = OrderState.DELIVERED;
        }
        System.out.println("Order state updated to: " + state);
    }

    public void cancel() throws CannotCancelFinalOrderException {
        if (state == OrderState.DELIVERED || state == OrderState.CANCELED) {
            throw new CannotCancelFinalOrderException();
        }
        history.push(state);
        state = OrderState.CANCELED;
        System.out.println("Order has been canceled.");
    }

    public void undoState() throws CannotRevertInitialOrderStateException {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }
        state = history.pop();
        System.out.println("Order state reverted to: " + state);
    }
}
