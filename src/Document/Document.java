package Document;

import Abonne.Abonne;

public interface Document {
	int numero();
	//return null si pas emprunt� ou pas r�serv�
	Abonne emprunteur() ; // Abonn� qui a emprunt� ce document
	Abonne reserveur() ; // Abonn� qui a r�serv� ce document
	//pre ni r�serv� ni emprunt�
	void reservationPour(Abonne ab) ;
	//pre libre ou r�serv� par l�abonn� qui vient emprunter
	void empruntPar(Abonne ab);
	//brief retour d�un document ou annulation d�une r�servation
	void retour();
}
