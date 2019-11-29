import java.util.Vector;

public interface Data <E> {
    /*
    OVERVIEW : Insieme modificabile di un dato con il relativo numero di like e un insieme di amici che lo hanno messo

    TYPICAL ELEMENT : <name,like,friends_i> con i >= 0

    REP INVARIANT : 0 <= i <= this.size
                    this.name != null &&
                    this.like>=0 &&
                    this.like = 0 <--> this.friends == null
                    this.like == this.friends.size

    AF : AF (this Data) = { friends_i con 0<= i <= frieds.size | l'oggetto contenuto in friends_i rappresenta un amico che ha messo like
                           e size il numero di like del dato }

    */

    //stampa a video il contenuto del dato
    //Requires : none
    //Modifies : none
    public void display();

    //restituisce il numero di like del dato
    //Requires : none
    //Modifies : none
    //Returns : this.like
    public int getLike();

    //restituisce il nome che identifica il dato
    //Requires : none
    //Modifies : none
    //returns : this.name
    public String getName();

    //aggiunge il like dell'amico al dato
    //Requires : friend != null
    //           !this.friends.contain(friend)
    //Modifies : this.like && this.friends
    //Effects : this.like_post == this.like_pre + 1 && this.friends_post = (this.friends_pre U friend)
    //Returns : none
    //Throws : friend == null --> IllegalArgumentException
    //         this.friends.contain (friend) --> FriendAlreadyLikedException
    public void addLike(String friend) throws FriendAlreadyLikedException;

    //setta il numero di like del dato
    //Requires : none
    //Modifies : this.like
    //Effects : this.like == like
    //Returns : none
    public void setLike(int like);

    //setta gli amici che hanno messo like
    //Requires : none
    //Modifies : this.friends
    //Effects : this.friends == friends
    //Returns : none
    public void setFriends(Vector<String> friends);

    //prende gli amici che hanno messo like al dato
    //Requires : none
    //Modifies : none
    //returns : this.friends
    public Vector<String> getFriends();

}