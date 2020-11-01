package tn.rnu.enit.ateliercorba.jcanon;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import tn.rnu.enit.ateliercorba.jcanon.communication.JCanonServerServant;


/**
 * Classe principale pour le lancement du serveur (interface graphique)
 * Le principe est de :
 *   1- initialisation de l'ORB
 *   2- se connecter au serveur de nom
 *   3- creer et publier le servant du client (pour le callback)
 *   4- recuperer une reference de l'objet distribue serveur
 *   5- lancer le jeur
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 00:43:00
 */
public class JCanonServer {
    public static void main(String[] args) {
        try {
            //initialisation de l'ORB
            ORB orb = ORB.init(args, null);
            //se connecter au serveur de nom
            NamingContext ncRef = NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));

            // creer et publier le servant du serveur
            JCanonServerServant ref = new JCanonServerServant();
            orb.connect(ref);
            ncRef.rebind(new NameComponent[]{new NameComponent("JCanonServer", "")}, ref);
            System.out.println("waiting for clients...");

            //verrouiller/bloquer la machine virtuelle pour que l'appel automatique de System.exit() ne soit pas invoque
            java.lang.Object sync = new java.lang.Object();
            synchronized (sync) {
                sync.wait();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
