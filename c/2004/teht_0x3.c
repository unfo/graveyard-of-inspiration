/****************************************************************************\
** 
** Tehtävä 3
** 
** Ohjelmassa luetaan kokonaislukuja taulukkoon, jonka maksimikoko on 20
** (annettu vakiona). Syöte loppuu luvullla 0 tai kun 20 lukua on syötetty. Jos
** syötetään luku, joka on suurempi kuin 100, annetaan ilmoitus "liian suuri
** luku" eikä sitä sijoiteta taulukkoon. Ohjelmassa on valikko, josta valitaan
** haluttu toiminto:
** 
** Ohjelma: a) tulostaa taulukossa olevat luvut
** b) laskee syötettyjen lukujen summan
** c) laskee negatiivisten lukujen määrän
** d) lopettaa
** 
\****************************************************************************/


#include<stdio.h>
#include<stdlib.h>

#define MAX_NUMBERS 20

void printMenu();

int main() {
	
	char choise[2];
	int numbers[MAX_NUMBERS];
	int sum = 0;
	int negatives = 0;
	int i;
	int lastIndex = -1;
	int temp;
	
	for (i = 0; i < MAX_NUMBERS; i++) {
		printf("Give integer #%d: ", i+1);
		scanf("%d", &temp);
		if (temp > 100) {
			printf("That was too BIG!");
			i--;
		} else {
			if (temp == 0) {
				lastIndex = i;
				break;
			} else {
				numbers[i] = temp;
			}
		}
	}
	lastIndex = lastIndex < 0 ? MAX_NUMBERS-1 : lastIndex;
	printMenu();
	scanf("%s", choise);

	while (choise[0] != 'd' && choise[0] != 'q') {
		switch (choise[0]) {
			case 'a':
				for (i = 0; i < lastIndex; i++)
					printf("%d ", numbers[i]);
				printMenu();
				scanf("%s", choise);
				break;
			case 'b':
				for (i = 0; i < lastIndex; i++)
					sum += numbers[i];
				printf("Sum: %d", sum);
				printMenu();
				scanf("%s", choise);
				break;
			case 'c':
				for (i = 0; i < lastIndex; i++)
					negatives += (numbers[i] < 0)? 1 : 0;
				printf ("%d negatives", negatives);
				printMenu();
				scanf("%s", choise);
				break;
			case '\n': printf("Bleeding newline\n");break;
			default:
				printf("Unknown choise");
				printMenu();
				scanf("%s", choise);
		}
		
	}
	
	
	
	return EXIT_SUCCESS;
}

void printMenu() {
	printf("\n-------------------------\n"
		   "\na) print numbers"
		   "\nb) count sum"
		   "\nc) count negative numbers"
		   "\nd) quit"
		   "\n-------------------------\n"
		   "\n> ");
}







