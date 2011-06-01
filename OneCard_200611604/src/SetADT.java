import java.util.Iterator;

public interface SetADT<T> {
	public void add(T element);
	public void remove(T element);
	public T removeRandom();
	public boolean find(T target);
	public T getCurrent();
	public boolean isEmpty();
	public int size();
	public Iterator<T> iterator();
}