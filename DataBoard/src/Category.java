import java.util.EmptyStackException;
import java.util.List;

public interface Category <E extends Data> {
    /*
    OVERVIEW : Collezione di dati di tipo generico che estendono Data (implementano Display), modificabile, con un sistma di privacy per la condivisione dei dati
                 e un insieme di amici con permesso di lettura
    TYPICAL ELEMENT : {<name, data_i, friends_k>} con i>=0

    REP INVARIANT : i >= 0 &&
                    friends.size >= 0
                    i >= 0 --> friends.size >= 0

    AF : AF(this category) = { data_i con 0 <= i < data.size | l'ogetto contenuto in data_i rappresenta un dato associato alla categoria name
                                e condiviso con gli amici friends }
    */


    //inserisce il dato
    //Requires : data != null
    //           data_i != data forall i>=0
    //Modifies : this.data
    //Effects : this.data_post = (this.data_pre U dato)
    //Returns : inserimento con successo --> true
    //          inserimento fallito --> false
    //Throws : data == null --> NullPointerException
    public boolean addData (E dato);

    //inserisce un amico
    //Requires : friend != null
    //           friend_i != friend forall i>=0
    //Modifies : this.friends
    //Effects : this.friends_post = (this.friends_pre U friend)
    //Returns : none
    //Throws : friend == null --> NullPointerException
    //         Esiste i >= 0 | friend_i == friend --> FriendAlreadyExistsException
    public void addFriend (String friend) throws FriendAlreadyExistsException;

    //rimuove un dato
    //Requires : dato != null
    //Modifies : this.data_i
    //Effects : this.data_i_post = (this.data_i_pre - dato)
    //Returns : none
    //Throws : dato == null --> NullPointerException
    public void removeData (E dato);

    //rimuove un amico
    //Requires : friend != null
    //Modifies : this.friend_i
    //Effects : this.friend_i_post = (this.friend_i_pre - dato)
    //Returns : none
    //Throws : friend == null --> NullPointerException
    public void removeFriend (String friend);

    //restituisce una copia del dato
    //Requires : dato != null
    //Modifies : none
    //Returns : this.dato_i
    //Throws : this.dato == null --> NullPointerException
    public E getData (E dato);

    public E getClone (E dato);

    //restituisce una lista dei dati contenuti
    //Requires : this.data != null
    //Modifies : none
    //Returns : List data
    //Throws : this.data == null --> EmptyStackException
    public List<E> listData () throws EmptyStackException;

    //restituisce il nome del dato
    //Requires : none
    //Modifies : none
    //Returns : name
    public String getName ();

    //restituisce il risultato della ricerca del dato
    //Requires : dato != null
    //Modifies : none
    //Returns : se trova dato --> true
    //          se non trova dato --> false
    //Throws : dato == null --> NullPointerException
    public boolean containDato (E dato);

    //restituisce il risultato della ricerca dell'amico
    //Requires : friend != null
    //Modifies : none
    //Returns : se trova friend --> true
    //          se non trova friend --> false
    //Throws : friend == null --> NullPointerException
    public boolean containFriend (String friend);

}
