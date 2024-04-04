package server.classes;

import commons.Participant;

public class ParticipantDebtPair implements Comparable<ParticipantDebtPair> {

    private Participant participant;
    private Double debt;

    public ParticipantDebtPair(Participant participant, Double debt) {
        this.participant = participant;
        this.debt = debt;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    @Override
    public int compareTo(ParticipantDebtPair o) {
        Double compareToAmount = o.getDebt();
        return -Double.compare(this.debt, compareToAmount);
    }
}
