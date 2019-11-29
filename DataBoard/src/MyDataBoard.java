
import java.util.*;

public class MyDataBoard <E extends Data> implements DataBoard <E> {

    final private Vector<Category <E> > categories;
    private String username;
    private String password;

    public MyDataBoard (String username, String password) {
        if (password == null) throw new NullPointerException();
        this.username = username;
        this.password = password;
        categories = new Vector<>();
    }

    public void createCategory(String category, String passw) throws WrongPasswordException, CategoryAlreadyExistsException {
        if (category == null || passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (IndexOf(category)>=0) throw new CategoryAlreadyExistsException ("Esiste già una Categoria "+category+", scegli un'altro nome");
        categories.addElement(new MyCategory<E>(category));
    }

    public void removeCategory(String category, String passw) throws WrongPasswordException {
        if (category == null || passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (IndexOf(category)!=-1) {
            categories.remove(IndexOf(category));
        }
    }

    public void addFriend(String category, String passw, String friend) throws WrongPasswordException, NotFoundCategoryException, FriendAlreadyExistsException {
       if (category == null || passw == null || friend == null) throw new NullPointerException();
       if (!this.password.equals(passw)) throw new WrongPasswordException();
       if (IndexOf(category)==-1) throw new NotFoundCategoryException("La categoria a cui vuoi aggiungere "+friend+" non esiste nella tua Bacheca!");
       categories.get(IndexOf(category)).addFriend(friend);
    }

    public void removeFriend(String category, String passw, String friend) throws WrongPasswordException {
        if (category== null || passw == null || friend == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (IndexOf(category) != -1) {
            categories.get(IndexOf(category)).removeFriend(friend);
        }
    }

    public boolean put(String passw, E dato, String category) throws WrongPasswordException, NotFoundCategoryException {
        if (category== null || passw == null || dato == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (IndexOf(category)==-1) throw new NotFoundCategoryException("La categoria a cui vuoi aggiungere il dato "+dato.getName()+" non esiste nella tua Bacheca!");
        return categories.get(IndexOf(category)).addData(dato);
    }

    public E get(String passw, E dato) throws NotFoundDataException, WrongPasswordException {
        if (passw == null || dato == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        int i=0;
        while (i< categories.size()) {
            if (categories.get(i).containDato(dato)) {
                return categories.get(i).getClone(dato);
            }
            i++;
        }
        throw new NotFoundDataException(dato.getName()+" non esiste nella tua DataBoard!");
    }

    public E remove(String passw, E dato) throws WrongPasswordException, NotFoundDataException {
        if (passw == null || dato == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        int i=0;
        E clone = null;
        while (i< categories.size()) {
            if (categories.get(i).containDato(dato)) {
                clone = categories.get(i).getClone(dato);
                categories.get(i).removeData(dato);
            }
            i++;
        }
        if (clone == null) throw new NotFoundDataException("Non esiste "+dato.getName()+" nella tua DataBoard!");
        return clone;
    }

    public void insertLike(String friend, E dato) throws FriendAlreadyLikedException, NotFoundDataException, FriendWithoutPermissionException {
        if (friend == null || dato == null) throw new NullPointerException();
        int i=0;
        int esiste = 0;
        while (i< categories.size()) {
            if (categories.get(i).containDato(dato)) {
                if (!categories.get(i).containFriend(friend)) throw new FriendWithoutPermissionException(friend+" non può mettere like");
                categories.get(i).getData(dato).addLike(friend);
                esiste = 1;
            }
            i++;
        }
        if (esiste == 0) throw new NotFoundDataException(dato.getName()+" non esiste nella tua DataBoard!");
    }

    public List <E> getDataCategory(String passw, String category) throws WrongPasswordException, NotFoundCategoryException {
        if (passw == null || category == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (IndexOf(category) < 0) throw new NotFoundCategoryException("La categoria "+category+" non esiste nella tua DataBoard!");
        if (categories.get(IndexOf(category)).listData().size()==0) throw new EmptyStackException();
        return categories.get(IndexOf(category)).listData();
    }

    public Iterator <E> getIterator(String passw) throws WrongPasswordException {
        if (passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        ArrayList <E> datiList = new ArrayList<>();
        int i = 0;
        while (i < categories.size()) {
            datiList.addAll(categories.get(i).listData());
            i++;
        }
        datiList.sort(new CustomComparator());
        if (datiList.size() == 0) throw new EmptyStackException();
        return Collections.unmodifiableList(datiList).iterator();
    }

    public Iterator <E> getFriendIterator(String friend) throws FriendNeverListedException {
        if (friend == null) throw new NullPointerException();
        int i=0;
        int esiste = 0;
        ArrayList <E> sharedList = new ArrayList<>();
        while (i < categories.size()) {
            if (categories.get(i).containFriend(friend)) {
                sharedList.addAll(categories.get(i).listData());
                esiste = 1;
            }
            i++;
        }
        if (esiste == 0) throw new FriendNeverListedException("Non hai condiviso alcun dato con "+friend);
        if (sharedList.size()==0) throw new EmptyStackException();
        return Collections.unmodifiableList(sharedList).iterator();
    }

    public int IndexOf(String category) {
        if (category == null) throw new NullPointerException();
        int i = 0;
        while (i< categories.size()) {
            if (categories.get(i).getName().equals(category)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getUsername () {
        return this.username;
    }


}
