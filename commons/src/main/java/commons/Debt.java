package commons;

import commons.primary_keys.DebtPK;
import jakarta.persistence.*;


@Entity
@Table(name = "debt")
public class Debt {
    @EmbeddedId
    private DebtPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expense_id")
    @JoinColumn(name = "expense_id")
    private Expense expense_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("participant_id")
    @JoinColumn(name = "participant_id")
    private Participant participant_id;

    @Column(name = "paid")
    private boolean paid;
}
