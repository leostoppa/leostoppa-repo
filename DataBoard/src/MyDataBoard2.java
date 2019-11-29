
import java.util.*;

public class MyDataBoard2 <E extends Data> implements DataBoard<E>{

    private HashMap<String,Category> categories;
    private String username;
    private String password;


    public MyDataBoard2 (String username, String password) {
        if (password == null) throw new NullPointerException();
        this.username = username;
        this.password = password;
        categories = new HashMap<>();
    }

    public void createCategory(String category, String passw) throws WrongPasswordException, CategoryAlreadyExistsException {
        if (category == null || passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (categories.containsKey(category)) throw new CategoryAlreadyExistsException ("Esiste già una Categoria "+category+", scegli un'altro nome");
        categories.put(category,new MyCategory(category));
    }

    public void removeCategory(String category, String passw) throws WrongPasswordException {
        if (category == null || passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        categories.remove(category);
    }

    public void addFriend(String category, String passw, String friend) throws WrongPasswordException, NotFoundCategoryException, FriendAlreadyExistsException {
        if (category == null || passw == null || friend == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (!categories.containsKey(category)) throw new NotFoundCategoryException("La categoria a cui vuoi aggiungere "+friend+" non esiste nella tua Bacheca!");
        categories.get(category).addFriend(friend);
    }

    public void removeFriend(String category, String passw, String friend) throws WrongPasswordException {
        if (category== null || passw == null || friend == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (categories.containsKey(category)) {
            categories.get(category).removeFriend(friend);
        }
    }

    public boolean put(String passw, E dato, String category) throws WrongPasswordException, NotFoundCategoryException {
        if (category== null || passw == null || dato == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (!categories.containsKey(category)) throw new NotFoundCategoryException("La categoria a cui vuoi aggiungere il dato "+dato.getName()+" non esiste nella tua Bacheca!");
        return categories.get(category).addData(dato);
    }

    public E get(String passw, E dato) throws NotFoundDataException, WrongPasswordException {
        if (passw == null || dato == null) throw new NullPointerException();
            if (!this.password.equals(passw)) throw new WrongPasswordException();
        E x = null;
        for (Category<E> cat : categories.values()) {
            if (cat.containDato(dato)) return cat.getClone(dato);
        }
        throw new NotFoundDataException(dato.getName()+" non esiste nella tua DataBoard!");
    }

    public E remove(String passw, E dato) throws WrongPasswordException, NotFoundDataException {
        if (passw == null || dato == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        E clone = null;
        for (Category<E> cat : categories.values()) {
            if (cat.containDato(dato)) {
                clone = cat.getClone(dato);
                cat.removeData(dato);
            }
        }
        if (clone == null) throw  new NotFoundDataException("Non esiste "+dato.getName()+" nella tua DataBoard!");
        return clone;
    }

    public void insertLike(String friend, E dato) throws FriendAlreadyLikedException, NotFoundDataException, FriendWithoutPermissionException {
        if (friend == null || dato == null) throw new NullPointerException();
        int esiste = 0;
        for (Category<E> cat : categories.values()) {
            if (cat.containDato(dato)) {
                if (!cat.containFriend(friend)) throw new FriendWithoutPermissionException(friend+" non può mettere like");
                esiste = 1;
                cat.getData(dato).addLike(friend);
            }
        }
        if (esiste == 0) throw new NotFoundDataException(dato.getName()+" non esiste nella tua DataBoard!");
    }

    public List <E> getDataCategory(String passw, String category) throws WrongPasswordException, NotFoundCategoryException {
        if (passw == null || category == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        if (!categories.containsKey(category)) throw new NotFoundCategoryException("La categoria "+category+" non esiste nella tua DataBoard!");
        if (categories.get(category).listData().size()==0) throw new EmptyStackException();
        return categories.get(category).listData();
    }

    public Iterator <E> getIterator(String passw) throws WrongPasswordException {
        if (passw == null) throw new NullPointerException();
        if (!this.password.equals(passw)) throw new WrongPasswordException();
        ArrayList <E> datiList = new ArrayList<>();
        for (Category<E> cat : categories.values()) {
            datiList.addAll(cat.listData());
        }
        if (datiList.size() == 0) throw new EmptyStackException();
        datiList.sort(new CustomComparator());
        return Collections.unmodifiableList(datiList).iterator();
    }

    public Iterator <E> getFriendIterator(String friend) throws FriendNeverListedException {
        if (friend == null) throw new NullPointerException();
        int i=0;
        int esiste = 0;
        ArrayList <E> sharedList = new ArrayList<>();
        for (Category<E> cat : categories.values()) {
            if (cat.containFriend(friend)) {
                sharedList.addAll(cat.listData());
                esiste = 1;
            }
        }
        if (esiste == 0) throw new FriendNeverListedException("Non hai condiviso alcuna Categoria di dati con "+friend);
        if (sharedList.size()==0) throw new EmptyStackException();
        return Collections.unmodifiableList(sharedList).iterator();
    }

    //Implemento il metodo per poter sfruttare la stessa specifica per le due implementazioni di DataBoard
    //HashMap non consente l'uso di un metodo IndexOf, quindi lancio solo un eccezione
    public int IndexOf(String category) {
        throw new UnsupportedOperationException("Il metodo non è supportato da MyDataBoard2!");
    }

    public String getUsername () {
        return this.username;
    }


}

