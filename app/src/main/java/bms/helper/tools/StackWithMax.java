package bms.helper.tools;
import java.util.Stack;

public class StackWithMax<E> extends Stack<E> {
    private int max=4;

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
    @Override
    public E push(E item) {
        if (size() + 1 > getMax()) {
            removeElementAt(0);
        }
        return super.push(item);
    }
    public StackWithMax() {}
    public StackWithMax(int max) {
        setMax(max);
    }


}
