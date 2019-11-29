

import java.util.Vector;

public class MyData <E> implements Data<E> {

    private String name;
    private int like;
    private Vector <String> friends;

    public MyData(String name) {
        if (name == null) throw new NullPointerException();
        this.name = name;
        like = 0;
        friends = new Vector<>();
    }

    public void display () {
        System.out.println("Nome : "+name);
        System.out.println("Numero di Like : "+like);
        if (like > 0) {
            System.out.println("Amici che hanno messo Like : "+friends);
        }
    }

    public String getName () {
        return this.name;
    }

    public int getLike () {
        return this.like;
    }

    public void addLike (String friend) throws FriendAlreadyLikedException  {
        if (friend == null ) throw new IllegalArgumentException();
        if (friends.contains(friend)) throw new FriendAlreadyLikedException("Hai già messo like a "+name);
        friends.addElement(friend);
        like++;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setFriends(Vector<String> friends) {
        this.friends = friends;
    }

    public Vector<String> getFriends() {
        return friends;
    }
}
