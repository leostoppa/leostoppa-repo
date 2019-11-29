import java.nio.file.AccessDeniedException;
import java.util.Iterator;
import java.util.List;

public interface DataBoard <E extends Data> {
    //OVEERVIEW : La collezione DataBoard<E extends Data> è un contenitore modificabile di oggetti generici che estendono il tipo di dato Data,
    //ognuno dei quali associato ad una categoria
    //TYPICAL ELEMENT : {<name, passw, Category_i} con 0<=i<Categorie.size

    //REP INVARIANT : 0 <= i < Category.size &&
    //                this.name != null && this.passw != null && Category != null &&
    //                this.name != null && this.passw != null --> 0 <= i < Category.size

    //AF : AF (DataBoard) = { Category_i con 0 <= i <Category.size | l'oggetto contenuto in Category_i rappresenta una categoria del proprietario name protetta dalla password passw}


    //Crea una categoria di dati se vengono rispettati i controlli di identità
    //Requires : category != null && passw != null && passw == this.passw && this.Categories_i != category forall 0<=i<Categories.size
    //Modifies : this
    //Effects : this.Categories_post = (this.Categories_pre U category)
    //Returns : none
    //Throws : category == null || passw == null --> NullPointerException
    //         passw != this.passw --> AccessDeniedException
    //         Esiste 0<=i<Categories.size | this.Categories_i == category --> CategoryAlreadyExistsException
    public void createCategory(String category, String passw) throws CategoryAlreadyExistsException, WrongPasswordException;

    //Rimuove una categoria di dati se vengono rispettati i controlli di identità
    //Requires : category != null && passw != null && passw == this.passw
    //Modifies : this
    //Effects : Categories_post = (Categories_pre - category)
    //Returns : none
    //Throws : category == null || passw == null --> NullPointerException
    //         passw != this.passw --> AccessDeniedException
    public void removeCategory(String category, String passw) throws WrongPasswordException;

    //Aggiunge un amico ad una categoria di dati se vengono rispettati i controlli di identità
    //Requires : category != null && passw != null && friend != null && this.passw == passw && Esiste 0<=i<Categories.size | Category_i == category
    //           && this.Categories(friend).friends_i != friend forall 0<=i<Categories.size
    //Modifies : this.Categories(friend).friends
    //Effects : this.Categories(friend).friends_post = this.Categories(friend).friends_pre U friend
    //Returns : none
    //Throws : category == null || passw == null || friend == null --> NullPointerException
    //         this.passw != passw --> AccessDeniedException
    //         !Esiste 0<=i<Categories.size | this.Categories == category --> NotFoundCategoryException
    //         Esiste 0<=i<Categories.size | this.Categories(friend).friends_i == friend --> FriendAlreadyExistsException
    public void addFriend(String category, String passw, String friend) throws  NotFoundCategoryException, FriendAlreadyExistsException, WrongPasswordException;

    //Rimuove un amico da una categoria di dati se vengono rispettati i controlli di identità
    //Requires : category != null && passw != null && friend != null && this.passw == passw
    //Modifies : this.Categories(friend).friends
    //Effects : this.Categories(friend).friends_post = this.Categories(friend).friends_pre - friend
    //Returns : none
    //Throws : category == null || passw == null || friend == null --> NullPointerException
    //         this.passw != passw --> AccessDeniedException
    public void removeFriend(String category, String passw, String friend) throws WrongPasswordException;

    //Inserisce un dato in bacheca se vengono rispettati i controlli di identità
    //Requires : category != null && passw != null && friend != null && this.passw == passw && Esiste 0<=i<Categories.size| Categories_i == category
    //Modifies : this.Categories(category).dati
    //Effects : this.Categories(category).dati_post = this.Categories(category).dati_pre U dato
    //Returns : risultato inserimento
    //Throws : category == null || passw == null || friend == null --> NullPointerException
    //         this.passw != passw --> AccessDeniedException
    //         !Esiste 0<=i<Categories.size | Categories_i == category --> NotFoundCategoryException
    public boolean put(String passw, E dato, String category) throws NotFoundCategoryException, WrongPasswordException;

    //Restituisce una copia del dato in bacheca se vengono rispettati i controlli di identità
    //Requires : passw != null && dato != null && this.passw == passw && Esiste 0<=i<categorie.size | this.Categories.category.dati_i == dato
    //Modifies : none
    //Returns : this.Categories(dato).dati_i con i | this.Categories(dato).dati_i == dato
    //Throws : passw == null || dato == null --> NullPointeException
    //         this.passw != passw --> AccessDeniedException
    //         !Esiste 0<=i<Categories.size | Categories(dato).dati_i == dato --> NotFoundDataException
    public E get(String passw,E dato) throws NotFoundDataException, WrongPasswordException;

    //Rimuove il dato dalla bacheca se vengono rispettati i controlli di identità
    //Requires : passw != null && dato != null && this.passw == passw && Esiste 0<=i<Categories.size | this.Categories(dato).dati_i == dato
    //Modifies : this.Categories(dato).dati
    //Effects : this.Categories(dato).dati_post == this.Categories(dato).dati_pre - dato
    //Returns : this.Categories(dato).dati_i con i | this.Categories(dato).dati_i == dato
    //Throws : passw == null || dato == null --> NullPointerException
    //         this.passw != passw --> AccessDeniedException
    //         !Esiste 0<=i<Categories.size | Categories(dato).dati_i == dato --> NotFoundDataException
    public E remove(String passw,E dato) throws NotFoundDataException, WrongPasswordException;

    //Crea la lista dei dati in bacheca di una determinata categoria se vengono rispettati i controlli di identità
    //Requires : passw != null && category != null && Esiste 0 <= i < Categories.size | Categories_i == category && Categories(category).dati != null && this.passw == passw
    //Modifies : none
    //Returns : Lista dei dati in bacheca
    //Throws : passw == null || category == null --> NullPointeException
    //         this.passw != passw -->AccessDeniedException
    //         !Esiste 0<=i<Categories.size | Categories_i == category --> NotFoundCategoryException
    //         Categories(category).dati.size == 0 --> EmptyStackException
    public List <E>  getDataCategory(String passw, String category) throws WrongPasswordException, NotFoundCategoryException;

    //Aggiunge un like a un dato se vengono rispettati i controlli di identità
    //Requires : friend != null && dato != null && this.Categories.category.contains(dato) && !this.Categories.category.dati.friends.contains(friend) && this.category(dato).contains(friend)
    //Modifies : this.Categories(dato).dato
    //Effects : this.Categories(dato).dato.like++ && this.Categories(dato).dato.friends_post = this.Categories(dato).dato.friends_pre U friend
    //Returns : null
    //Throws : this.Categories(dato).dati.friends.contains(friend) --> FriendAlreadyLikedException
    //         passw == null || category == null --> NullPointeException
    //         !this.Categories(dato).contains(dato) --> NotFoundDataException
    //         !this.category(dato).contains(friend) --> FriendWithoutPermissionException
    void insertLike(String friend,E dato) throws FriendAlreadyLikedException, NotFoundDataException, FriendWithoutPermissionException;

    //Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca ordinati rispetto al numero di like se vengono rispettati i controlli di identità
    //Requires : passw != null && Esiste 0<=i<Categories.size | Categories_i.dati.size > 0
    //Modifies : none
    //Returns : Iterator dati in bacheca
    //Throws : passw == null --> NullPointerException
    //         Categories_i.dati.size == 0 forall 0<=i<=category.size --> EmptyStackException
    public Iterator<E> getIterator(String passw) throws WrongPasswordException;

    //Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca condivisi
    //Requires : friend != null && Categories.category(friend).dati.size > 0 && Esiste 0<=i<Categories.size | Categories_i.contains(friend)
    //Modifies : none
    //Returns : Iterator dati condivisi con l'amico friend
    //Throws : friend == null --> NullPointerException
    //         Categories_i.dati.size == 0 forall i==indexof(friend) --> EmptyStackException
    //         !Esiste 0<=i<Categories.size | Categories_i.contains(friend) --> FriendNeverListedException
    public Iterator<E> getFriendIterator(String friend) throws FriendNeverListedException;

    // … altre operazione da definire a scelta

    //restituisce l'indice di category
    //Requires : catgeory != null
    //Modifies : none
    //Returns : i | Categories_i.name == category
    //Throws : category == null --> NullPointerException
    public int IndexOf (String category);

    //restituisce l'username del proprietario della DataBoard
    //Requires : none
    //Modifies : none
    //Returns : username
    public String getUsername ();
}
