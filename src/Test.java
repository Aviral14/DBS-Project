import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Set<Integer> a = new HashSet<>();
        a.add(1);
        a.add(2);
        a.add(3);
        Set<Integer> b = new HashSet<>();
        b.add(4);
        b.add(5);
        Set<Integer> c = new HashSet<>(a);
        c.addAll(b);
        System.out.println();
    }
}
