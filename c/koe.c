#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

unsigned int countalpha(void) {
	unsigned int chars = 0;
	unsigned int alphas = 0;

	while (chars < 10) {
		printf("%d/10: ", (chars+1));
		char* c = calloc(sizeof(char), 3);
		gets(c);
		if (strlen(c) > 0) {
			chars++;
			if (isalpha(c[0]))
				alphas++;
		}

		printf("\n");
	} 
	return alphas;
}
int calcsum(void) {
	int sum = 0;
	unsigned int nums = 10;
	while (nums) {
		printf("Anna luku> ");
		int num;
		scanf("%d", &num);
		sum += num;
		nums--;
	}

	return sum;
}
unsigned int lenstr(const char* str) {
	unsigned int len = 0;

	while (str[len++]);
	len--; // by default if while broke => str[len] = \0 => minus one

	return len;
}

void make7bit(unsigned char* s, unsigned int n) {
	unsigned int i = 0;
	while (i < n) {
		s[i++] &= 0x7F; // 0111 1111
	}
}
int main(void) 
{
	int a = (8 < 9); 
	int b; b = 5; while (b > 3) b--;
	int c = strlen("text");
	float d = 7.0 / 2;
	int e = 0xACDC & 0x00FF ;// 0x0101 | 0x1010;
	int arr[] = {5,20,40}; int *p = arr; p++; int f = *p;
	printf("a: %d = 1 ; b: %d = 3; c: %d = 11; d: %f = 3.0; e: %x = 0x00DC; f: %d = 20;\n", a,b,c,d,e,f);

	//printf("Summa: %d\n", calcsum());
	printf("strlen(kekbur) = %lu ; lenstr(kekbur) = %u\n", strlen("kekbur"), lenstr("kekbur"));
	unsigned char c23 = 0xA3;
	unsigned char c7e = 0x7E;

	make7bit(&c23, 1);
	make7bit(&c7e, 1);
	printf("make7bit 0xA3 => 0x23 == %x ;; 0x7E => 0x7E == %x\n", c23, c7e);

	int i = 7;
	printf("%d\n", i++ * i++);
}

