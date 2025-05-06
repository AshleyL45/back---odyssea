package com.example.odyssea.entities;

public class ReservationOptionDraft {
    private int reservationDraftId;
    private int optionId;


    public ReservationOptionDraft() {
    }

    public ReservationOptionDraft(int reservationDraftId, int optionId) {
        this.reservationDraftId = reservationDraftId;
        this.optionId = optionId;
    }

    public int getReservationDraftId() {
        return reservationDraftId;
    }

    public void setReservationDraftId(int reservationDraftId) {
        this.reservationDraftId = reservationDraftId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}

