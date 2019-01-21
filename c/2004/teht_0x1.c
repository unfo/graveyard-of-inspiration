/*****************************************************************************\
| teht‰v‰ 1
| Yrityksess‰ maksetaan kilometrikorvauksia kuukausittaisen kilometrim‰‰r‰n
| mukaan seuraavasti.
| 
| Kilometrej‰ alle 1000 korvaus 0.5 e/ km
| kilometrej‰ 1000-3000 korvaus 0.3e / km
| Kilometrej‰ > 3000    krovaus 0.2e / km
| 
| Tee c-kielinen ohjelma, joka kysyy kilometrim‰‰r‰n ja tulostaa
| kilometrim‰‰r‰n ja kilometrim‰‰r‰lle korvauksen. lis‰ksi tulostetaan
| korvaus/kilometri.
\*****************************************************************************/


#include<stdio.h>
#include<stdlib.h>
#define ENS_LKA 0.5 	/* ensimmainen korvausluokka */
#define TOINEN_LKA 0.3 	/* toinen luokka */
#define KOLMAS_LKA 0.2 	/* kolmas luokka */
/* funktio palauttaa 0.2,0.3 tai 0.5 */
double travelPay(int* km, double* payment);

int main() {
	int km = 0;
	double perKM = 0.0;
	double payment = 0.0;
	
	printf("Matkakorvauslaskuri\n"
		   "-------------------\n"
		   "Anna kilometrit: ");
	scanf("%d", &km);
	perKM = travelPay(&km, &payment);
	
	printf("\n%d kilometrilta saat korvausta: %.2f euroa\n", km, payment);
	printf("Korvausta per km saat: %.2f e\n", perKM);
	
	return EXIT_SUCCESS;
}

double travelPay(int* km, double* payment) {
	if (*km < 1000) {
		*payment = (*km)*ENS_LKA;
		return ENS_LKA;
	} else {
		if (*km < 3000) {
			*payment = (*km)*TOINEN_LKA;
			return TOINEN_LKA;
		} else {
			*payment = (*km)*KOLMAS_LKA;
			return KOLMAS_LKA;
		}
	}
	return 0.0;
}
