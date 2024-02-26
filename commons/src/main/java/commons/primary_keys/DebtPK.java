package commons.primary_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class DebtPK implements Serializable {
    @Column(name = "expense_id")
    private UUID expense_id;

    @Column(name = "participant_id")
    private UUID participant_id;

}
