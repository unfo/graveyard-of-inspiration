#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#define MAX_NUMBERS 10000	 /* maximum quantity of numbers 				*/
#define M 1000000
#define TRUE 1
#define FALSE 0


int main() {

	srand(time(NULL)); /* Seeding the random numbers */
	struct timeval startPointers, stopPointers, startData, stopData;
	unsigned int *pointers[MAX_NUMBERS] = { NULL }; /* These are used when we bubblesort with ptrs */
	unsigned int numbers[MAX_NUMBERS] = { 0 }; /* These are used when we bubblesort with data */
	unsigned int *pTemp = NULL; /* temp pointer for swapping */
	unsigned int nTemp = 0; /* temp int for swapping */
	int i = 0;
	int sorted = FALSE;
	while (i < MAX_NUMBERS) {
		pointers[i] = malloc(sizeof(unsigned int));
		if (pointers[i] == NULL) {
			printf("Memory could not be allocated. Exiting...");
			return;
		}
		unsigned int randomValue = (unsigned int) rand();
		*pointers[i] = randomValue;
		numbers[i++] = randomValue; /* make sure the counter gets incremented */
	}
	gettimeofday(&startData, NULL);
	while (!sorted) {
		sorted = TRUE;
		for (i = 0; i < MAX_NUMBERS-1; i++) {
			if (numbers[i] > numbers[i+1]) {
				sorted = FALSE;
				nTemp = numbers[i];
				numbers[i] = numbers[i+1];
				numbers[i+1] = nTemp;
			}
		}
	}
	gettimeofday(&stopData, NULL);
		sorted = FALSE;
	gettimeofday(&startPointers, NULL);
	while (!sorted) {
		sorted = TRUE;
		for (i = 0; i < MAX_NUMBERS-1; i++) {
			if (*pointers[i] > *pointers[i+1]) {
				sorted = FALSE;
				pTemp = pointers[i];
				pointers[i] = pointers[i+1];
				pointers[i+1] = pTemp;
			}
		}
	}
        gettimeofday(&stopPointers, NULL);
		

	printf("With %d random unsigned integers\n"
		"Bubblesort with pointers took: \t\t%d microseconds\n"
		"Bubblesort with data moving took: \t%d microseconds\n", MAX_NUMBERS,
		(stopPointers.tv_sec-startPointers.tv_sec)*M+stopPointers.tv_usec-startPointers.tv_usec,
		(stopData.tv_sec-startData.tv_sec)*M+stopData.tv_usec-startData.tv_usec);
	return EXIT_SUCCESS;
}

