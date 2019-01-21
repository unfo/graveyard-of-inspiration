#include<stdio.h>
#include<stdlib.h>

int main() {
	unsigned int i = 0;
	int random_integer = 0;
	srand((int) time(NULL));
	while (i++ < 5) {
		random_integer = 100 + (int) (31.0 * rand()/(RAND_MAX+1.0));
		printf("%d\n", random_integer);
	}
	return 0;
}


