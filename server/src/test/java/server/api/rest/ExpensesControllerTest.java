package server.api.rest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import server.api.rest.ExpensesController;
import server.services.ExpenseService;

public class ExpensesControllerTest {

    @Mock
    private ExpenseService service;
    @InjectMocks
    private ExpensesController sut;

}
