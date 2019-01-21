#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

int success() {
    printf("For Great Success!\n");
    return 42;
}

int failure() {
    printf("I SET US UP THE BOMB\n");
    return 666;
}

int main() {
    printf("Secret key> ");

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

    unsigned int magic = 7;
    int retval = 0;

    if (bytecount == magic) {
        unsigned int correct = 0;
        for (unsigned int i = 0; i < magic; i++) {
            if ((line[i] ^ line[magic-1-i]) == 0) {
                correct++;
            }
        }
        if (correct == magic) {
            retval = success();
        } else {
            retval = failure();
        }
    } else {
        retval = failure();
    }

    free(line);

    return retval;
}