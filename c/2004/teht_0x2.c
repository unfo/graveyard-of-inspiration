/***************************************************************************\
** Teht‰v‰ 2
** 
** Tee ohjelma, joka kysyy kymmenen lukua ja tulostaa negatiivisten lukujen
** m‰‰r‰n ja keskiarvon, positiivisten lukujen lukum‰‰r‰n ja keskiarvon sek‰
** nollien lukum‰‰r‰n.
** 
\***************************************************************************/

#include<stdio.h>
#include<stdlib.h>

#define MAX_NUMBERS 10

int main() {
	int posSum = 0;
	int negSum = 0;
	int posCount = 0;
	int negCount = 0;
	int zeroCount = 0;
	int i;
	int temp;
	double avg = 0.0;
	for (i = 0; i < MAX_NUMBERS; i++) {
		printf("Give an%s integer: ", (i > 0)?"other":"");
		scanf("%d", &temp);
		if (temp > 0) {
			posSum += temp;
			posCount++;
		} else {
			if (temp < 0) {
				negSum -= temp;
				negCount++;
			} else {
				zeroCount++;
			}
		}
	}
	printf("Total of %d negatives", negCount);
	if (negCount > 0) {
		avg = negSum/(double)negCount;
		printf(", negSum=%d negCount=%d => avg %.2f", negSum, negCount, avg);
	}
	printf("\nTotal of %d positives", posCount);
	if (posCount > 0) {
		avg = posSum/(double)posCount;
		printf(", posSum=%d posCount=%d => avg %.2f", posSum, posCount, avg);
	}
	printf("\nTotal of %d zeros\n", zeroCount);
	avg = 0.0;
	return EXIT_SUCCESS;
}
