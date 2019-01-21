#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>

char* reverse(const char* line);
char* sort(const char* line);

int main(void)
{

	printf("Input> ");
	unsigned int MAX_CHARS = 100;
	char* line = calloc(sizeof(char), MAX_CHARS + 1);

	unsigned int bytecount = 0;
	char chr;
	do {
		int read = scanf("%c", &chr);
		if (read == 1 && chr != '\n' && chr != EOF) {
			line[bytecount++] = chr;
		} else {
			break;
		}

	} while (bytecount < MAX_CHARS);


	line[bytecount] = '\0';
	printf(" length(%s) = %d\n", line, bytecount);
	printf("reverse(%s) = %s\n", line, reverse(line));
	printf("   sort(%s) = %s\n", line, sort(line));

	free(line);

	return 0;
}

char* reverse(const char* line)
{
	unsigned int len = strlen(line);
	char* rev = calloc(sizeof(char), len);
	for (unsigned int i = 1; i <= len; i++)
		rev[i-1] = line[len-i];

	return rev;

}
char* sort(const char* line)
{
	unsigned int len = strlen(line);
	char* sorted = calloc(sizeof(char), len);
	for (unsigned int i = 0; i < len; i++)
		sorted[i] = line[i];

	for (unsigned int i = 0; i < len; i++) {
		 for (unsigned int j = len-1; j > i; j--) {
			if (sorted[i] > sorted[j]) {
				char tmp = sorted[i];
				sorted[i] = sorted[j];
				sorted[j] = tmp;
			}
		}
	}

	return sorted;
}
