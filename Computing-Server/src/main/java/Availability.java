public class Availability {
    private Float value;

    public Availability() {
        this.value = 0f;
    }

    public Availability(Float value) {
        this.value = value;
    }

    public synchronized Float getValue() {
        return value;
    }

    public synchronized void increaseValueBy(Float value) {
        this.value += value;
    }

    public synchronized void decreaseValueBy(Float value){
        this.value -= value;
        if (this.value < 0)
            this.value = 0f;
    }
}
