import java.util.Comparator;

public class CustomComparator implements Comparator <Data> {
    @Override
    public int compare(Data o1, Data o2) {
        return Integer.compare(o1.getLike(), o2.getLike());
    }
}
