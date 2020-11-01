package tn.rnu.enit.ateliercorba.jcanon;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NameComponent;
import tn.rnu.enit.ateliercorba.jcanon.communication.JCanonClientServant;
import jcanon.*;

/**
 * Classe principale pour le lancement d'un client (interface graphique)
 * Le principe est de :
 *   1- initialisation de l'ORB
 *   2- se connecter au serveur de nom
 *   3- creer et publier le servant du client (pour le callback)
 *   4- recuperer une reference de l'objet distribue serveur
 *   5- lancer le jeur
 *   la fonction main s'attend a un parametre qui est le numero du client (1 ou 2)
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 00:43:00
 */
public class JCanonClient {
    public static void main(String[] args) {
        try {
            //initialisation de l'ORB
            ORB orb = ORB.init(args, null);
            //se connecter au serveur de nom
            NamingContext ncRef = NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));

            // creer et publier le servant du client (pour le callback)
            // un identifiant aleatoire est cree
            String clientIdentifier="JCanonClient"+Math.random();
            JCanonClientServant ref = new JCanonClientServant();
            orb.connect(ref);
            ncRef.rebind(new NameComponent[]{new NameComponent(clientIdentifier, "")}, ref);

            // recuperer une reference de l'objet distribue serveur
            final jcanon.JCanonServer server = JCanonServerHelper.narrow(ncRef.resolve(new NameComponent[]{new NameComponent("JCanonServer", "")}));

            //lancer le jeur
            ref.startGame(server);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
