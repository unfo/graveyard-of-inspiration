#include<stdio.h>
#include <time.h>
#include<stdlib.h>
#define MAX_LETTERS 50

int CmpFunc(const void* _a, const void* _b) {
	// you've got to explicitly cast to the correct type
	const char* a = (const char*) _a;
	const char* b = (const char*) _b;
      	if(*a > *b) return 1;              // first item is bigger than the second one -> return 1
	else
		if(*a == *b) return  0;         // equality -> return 0
		else         return -1;         // second item is bigger than the first one -> return -1
}
int main() {
	unsigned int i = 0;
	int j = 0;
	char alphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	char randString[MAX_LETTERS];
	srand((int) time(NULL));
	char string[MAX_LETTERS];
	printf("Enter a string to be sorted alphabetically:\n");
	while (i < MAX_LETTERS) {
		char temp = getchar();
		if (temp != '\n')
			string[i++] = temp;
		else
			break;
	}
	printf("Sorted string:\n");
//	qsort((void*) field, /*number of items*/ 100, /*size of an item*/ sizeof(field[0]), /*comparison-function*/ CmpFunc);
	qsort((void*) string, i, sizeof string[0], CmpFunc);
	for (; j < i; j++) {
		printf("%c", string[j]);
	}
	printf("\n");
	
	return 0;
	
}


