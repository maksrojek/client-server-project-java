/**
 * Store data to compute.
 * @param <T> data type
 */
public class ComputeData<T> {
    private T data;

    public ComputeData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
