package commons.views;

public class View {
    public static final int MAX_STRING = 255;
    public interface CommonsView {}
    public interface ExpenseView extends CommonsView {}
    public interface StatisticsView extends CommonsView {}
    public interface ParticipantView extends CommonsView {}
    public interface OverviewView extends CommonsView, ExpenseView {}
    public interface SettleView extends CommonsView {}
}
