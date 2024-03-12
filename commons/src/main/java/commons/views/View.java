package commons.views;

public class View {
    public static final int MAX_STRING = 255;
    public interface CommonsView {}
    public interface ParticipantView extends CommonsView {}
    public interface ExpenseView extends CommonsView {}
    public interface OverviewView extends CommonsView {}
    public interface SettleView extends CommonsView {}
    public interface DebtView extends SettleView {}
}
