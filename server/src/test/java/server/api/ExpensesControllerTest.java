package server.api;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import server.services.ExpenseService;

public class ExpensesControllerTest {

    @Mock
    private ExpenseService service;
    @InjectMocks
    private ExpensesController sut;

}
