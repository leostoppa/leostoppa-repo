import java.util.*;

public class MyCategory <E extends Data > implements Category <E> {


    final private Vector<E> dati;
    final private Vector<String> friends;
    private String name;

    public MyCategory(String name) {
        if (name == null) throw new NullPointerException();
        this.name = name;
        dati = new Vector<>();
        friends = new Vector<>();
    }

    public boolean addData(E dato) {
        if (dato == null) throw new NullPointerException();
        if (dati.contains(dato)) return false;
        dati.addElement(dato);
        return true;
    }

    public void addFriend(String friend) throws FriendAlreadyExistsException {
        if (friend == null) throw new NullPointerException();
        if (friends.contains(friend)) throw new FriendAlreadyExistsException("Esiste già un amico "+friend+", prova con un'altro nome");
        friends.addElement(friend);
    }

    public void removeData(E dato) {
        if (dato == null) throw new NullPointerException();
        dati.remove(dato);
    }

    public void removeFriend(String friend) {
        if (friend == null) throw new NullPointerException();
        friends.remove(friend);
    }

    public E getData(E dato) {
        if (dato==null) throw new NullPointerException();
        return dati.get(dati.indexOf(dato));
    }

    public E getClone (E dato) {
        if (dato == null) throw new NullPointerException();
        int i = dati.indexOf(dato);
        E clone = (E) new MyData<E>(dati.get(i).getName());
        clone.setLike(dati.get(i).getLike());
        clone.setFriends(dati.get(i).getFriends());
        return clone;
    }

    public List<E> listData() {
        return dati;
    }

    public String getName () {
        return name;
    }

    public boolean containDato (E dato) {
        if (dato == null) throw new NullPointerException();
        return dati.contains(dato);
    }

    public boolean containFriend (String friend) {
        if (friend == null) throw new NullPointerException();
        return friends.contains(friend);
    }

}
