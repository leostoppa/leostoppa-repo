
import java.util.Iterator;

public class DataBoardTest {
    public static void main(String[] args) {

        //Per testare la prima implementazione di DataBoard commentare il secondo assegnamento
        //Per testare la seconda implementazione di DataBoard commentare il primo assegnamento

        //DataBoard <Data> dataBoardleo = new MyDataBoard<>("leostoppa","leo99");
        DataBoard <Data> dataBoardleo = new MyDataBoard2<>("leostoppa","leo99");


        System.out.println("\nPROVO A CREARE UNA CATEGORIA SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.createCategory("Foto", "leo");
        }catch (WrongPasswordException | CategoryAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("CREO UNA CATEGORIA");
        try {
            dataBoardleo.createCategory("foto","leo99");
        }catch (WrongPasswordException | CategoryAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A CREARE UNA CATEGORIA GIA' ESISTENTE");
        try {
            dataBoardleo.createCategory("foto","leo99");
        }catch (WrongPasswordException | CategoryAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("\nCREO UNA CATEGORIA CHE ANDRO' A RIMUOVERE");
        try {
            dataBoardleo.createCategory("video","leo99");
        }catch (WrongPasswordException | CategoryAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A RIMUOVERE L'ULTIMA CATEGORIA CREATA SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.removeCategory("video","leo");
        }catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("RIMUOVO L'ULTIMA CATEGORIA CREATA");
        try {
            dataBoardleo.removeCategory("video","leo99");
        }catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO AD AGGIUNGERE UN AMICO SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.addFriend("foto","leo","fede");
        } catch (WrongPasswordException | NotFoundCategoryException | FriendAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO AD AGGIUNGERE UN AMICO AD UNA CATEGORIA INESISTENTE");
        try {
            dataBoardleo.addFriend("video","leo99","fede");
        } catch (WrongPasswordException | NotFoundCategoryException | FriendAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("AGGIUNGO UN AMICO");
        try {
           dataBoardleo.addFriend("foto","leo99","fede");
        }catch (WrongPasswordException | NotFoundCategoryException | FriendAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO AGGIUNGERE UN AMICO GIA' PRESENTE NELLA CATEGORIA");
        try {
            dataBoardleo.addFriend("foto","leo99","fede");
        }catch (WrongPasswordException | NotFoundCategoryException | FriendAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("\nAGGIUNGO UN AMICO CHE ANDRO' A RIMUOVERE");
        try {
            dataBoardleo.addFriend("foto","leo99","pippo");
        } catch (WrongPasswordException | NotFoundCategoryException | FriendAlreadyExistsException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A RIMUOVERE L'ULTIMO AMICO CREATO SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.removeFriend("foto","leo","pippo");
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("RIMUOVO L'ULTIMO AMICO CREATO");
        try {
            dataBoardleo.removeFriend("foto","leo99","pippo");
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("\nCREO UN DATO");
        Data <Data> dato1 = new MyData<>("fotomare");

        System.out.println("PROVO AD AGGIUNGERE IL DATO SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.put("leo",dato1,"foto");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO AD AGGIUNGERE IL DATO IN UNA CATEGORIA CHE NON ESISTE");
        try {
            dataBoardleo.put("leo99",dato1,"video");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("AGGIUNGO IL DATO");
        try {
            dataBoardleo.put("leo99",dato1,"foto");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO A PRENDERE L'ULTIMO DATO AGGIUNTO SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.get("leo",dato1);
        } catch (WrongPasswordException | NotFoundDataException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A PRENDERE UN DATO CHE NON ESISTE");
        Data <Data> dato2 = new MyData<>("selfie");
        try {
            dataBoardleo.get("leo99",dato2);
        } catch (WrongPasswordException | NotFoundDataException e) {
            e.printStackTrace();
        }

        System.out.println("PRENDO IL DATO INSERITO");
        try {
            dataBoardleo.get("leo99",dato1);
        } catch (WrongPasswordException | NotFoundDataException e) {
            e.printStackTrace();
        }

        System.out.println("\nAGGIUNGO UN DATO DA RIMUOVERE");
        try {
            dataBoardleo.put("leo99",dato2,"foto");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A RIMUOVERE L'ULTIMO DATO AGGIUNTO SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.remove("leo",dato2);
        } catch (NotFoundDataException | WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A RIMUOVERE UN DATO CHE NON ESISTE");
        Data<Data> dato3 = new MyData<>("tramonto");
        try {
            dataBoardleo.remove("leo99",dato3);
        } catch (NotFoundDataException | WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("RIMUOVO L'ULTIMO DATO AGGIUNTO");
        try {
            dataBoardleo.remove("leo99",dato2);
        } catch (NotFoundDataException | WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO A METTERE LIKE A UN DATO CHE NON ESISTE");
        try {
            dataBoardleo.insertLike("fede",dato2);
        } catch (FriendAlreadyLikedException | NotFoundDataException | FriendWithoutPermissionException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A METTERE LIKE CON UN AMICO CHE NON PUO'");
        try {
            dataBoardleo.insertLike("pippo",dato1);
        } catch (FriendAlreadyLikedException | NotFoundDataException | FriendWithoutPermissionException e) {
            e.printStackTrace();
        }

        System.out.println("METTO LIKE A UN DATO");
        try {
            dataBoardleo.insertLike("fede",dato1);
        } catch (FriendAlreadyLikedException | NotFoundDataException | FriendWithoutPermissionException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A METTERE LIKE A UN DATO CON UN AMICO CHE HA GIA' MESSO LIKE");
        try {
            dataBoardleo.insertLike("fede",dato1);
        } catch (FriendAlreadyLikedException | NotFoundDataException | FriendWithoutPermissionException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO A PRENDERE LA LISTA DI TUTTI I DATI INSERITI IN UNA CATEGORIA SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.getDataCategory("leo","foto");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("PROVO A PRENDERE LA LISTA DI TUTTI I DATI INSERITI IN UNA CATEGORIA CHE NON ESISTE");
        try {
            dataBoardleo.getDataCategory("leo99","video");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("PRENDO UNA LISTA DI TUTTI I DATI INSERITI IN UNA CATEGORIA");
        try {
            dataBoardleo.getDataCategory("leo99","foto");
        } catch (WrongPasswordException | NotFoundCategoryException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO A PRENDERE UN ITERATORE DI TUTTI I DATI INSERITI ORDINATI PER LIKE SBAGLIANDO PASSWORD");
        try {
            dataBoardleo.getIterator("leo");
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("PRENDO UN ITERATORE DI TUTTI I DATI INSERITI ORDINATI PER LIKE");
        try {
            dataBoardleo.getIterator("leo99");
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        }

        System.out.println("\nPROVO A PRENDERE UN ITERATORE DI TUTTI I DATI CONDIVISI CON UN AMICO CON CUI NON HO CONDIVISO ALCUN DATO");
        try {
            dataBoardleo.getFriendIterator("pippo");
        } catch (FriendNeverListedException e) {
            e.printStackTrace();
        }

        System.out.println("PRENDO UN ITERATORE DI TUTTI I DATI CONDIVISI CON UN AMICO");
        Iterator<Data> iterator = null;
        try {
            iterator = dataBoardleo.getFriendIterator("fede");
        } catch (FriendNeverListedException e) {
            e.printStackTrace();
        }

        System.out.println("CONTROLLO CHE L'ITERATORE DEI DATI CONDIVISI CON L'AMICO PRESO PRIMA STAMPI I DATI CORRETTAMENTE");
        assert iterator != null;
        while (iterator.hasNext()) {
            Data dato = iterator.next();
            dato.display();
        }
        System.out.println("\nSTAMPO A VIDEO CON IL METODO DISPLAY IL DATO INSERITO ALL'INIZIO");
        dato1.display();

        System.out.println("\nFINE");
    }
}