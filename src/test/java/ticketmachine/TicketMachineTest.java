package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TicketMachineTest {
	private static final int PRICE = 50; // Une constante

	private TicketMachine machine; // l'objet à tester

	@BeforeEach
	public void setUp() {
		machine = new TicketMachine(PRICE); // On initialise l'objet à tester
	}

	@Test
	// On vérifie que le prix affiché correspond au paramètre passé lors de
	// l'initialisation
	// S1 : le prix affiché correspond à l’initialisation.
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur effective, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent
	void insertMoneyChangesBalance() {
		// GIVEN : une machine vierge (initialisée dans @BeforeEach)
		// WHEN On insère de l'argent
		machine.insertMoney(10);
		machine.insertMoney(20);
		// THEN La balance est mise à jour, les montants sont correctement additionnés
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
	// S3 : on n’imprime pas le ticket si le montant inséré est insuffisant
	void dontPrintTicketTicketIfBalanceIsInsufficient() {
		// GIVEN : une machine avec un prix de 50
		// WHEN : on insère 49 pour tester les limites
		machine.insertMoney(PRICE-1);
		// THEN on ne peut pas imprimer le ticket
		assertFalse(machine.printTicket(), "On a pu imprimer le ticket alors que le montant est insuffisant");
	}

	@Test
	//S4 : on imprime le ticket si le montant inséré est suffisant
	void printTicketIfBalanceIsSufficient() {
		machine.insertMoney(PRICE+1);
		assertTrue(machine.printTicket(), "On n'a pas pu imprimer le ticket alors que le montant est suffisant");
	}

	@Test
	//S5 : Quand on imprime un ticket la balance est décrémentée du prix du ticket
	void DecreaseBalanceWhenTicketIsPrinted() {
		machine.insertMoney(51);
		machine.printTicket();
		assertEquals(51-PRICE, machine.getBalance(), "Le montant de la balance n'est pas décrémenté quand on imprime le ticket");
	}

	@Test
	// S6 : le montant collecté est mis à jour quand on imprime un ticket (pas avant)
	void TotalIsUpdatedOnlyOnceTicketIsPrinted() {
		int total=machine.getTotal();
		machine.insertMoney(51);
		assertEquals(machine.getTotal(), total );
        machine.printTicket();
		assertEquals(machine.getTotal(), machine.getPrice(), "Le montant collecté est mis à jour avant que le ticket ne soit imprimé ");
	}
	@Test
	// S7 : refund() rend correctement la monnaie
	void RefundIsWorkingCorrectly(){
		machine.insertMoney(60);
		assertEquals(machine.refund(), 60, "Le montant de la balance n'a pas été correctement remboursé");

	}

	@Test
	// S8 : refund() remet la balance à zéro
	void RefundSetsBalanceToZero() {
		machine.insertMoney(51);
		machine.refund();
		assertEquals(machine.getBalance(), 0);
	}

	@Test
	// S9 : on ne peut pas insérer un montant négatif
	void NegativeAmountCantBeInserted() {
		try {
			machine.insertMoney(-1);
			fail();
		}
		catch (IllegalArgumentException e) {

		}
	}

	@Test
	// S10 : on ne peut pas créer de machine qui délivre des tickets dont le prix est négatif
	void CantCreateMachineWithNegativeTicketPrice() {
		try {
			new TicketMachine(-1);
			fail();
		}
		catch (IllegalArgumentException e) {

		}
	}
}
