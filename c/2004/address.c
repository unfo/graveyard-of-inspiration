#include<stdio.h>
#include<stdlib.h>

int main() {
	long a = 123L;
	long b = 456L;
	long c = 789L;
	
	double d = 1.0;
	double e = 2.0;
	double f = 3.0;
	
	printf("\nThe address of a is : %p", &a);
	printf("\nThe address of b is : %p", &b);
	printf("\nThe address of c is : %p", &c);
	printf("\nThe address of d is : %p", &d);
	printf("\nThe address of e is : %p", &e);
	printf("\nThe address of f is : %p", &f);
	return EXIT_SUCCESS;
}
