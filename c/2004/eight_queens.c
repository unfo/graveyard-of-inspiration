/*****************************************************************************\
** 																			 **
** 3. EIGHT QUEENS PROBLEM													 **
** 																			 **
** Write a program to determine all solutions to place 8 queens on a		 **
** chess board without attacking one another.								 **
** 																			 **
** Code: Jan Wikholm, 2004													 **
\*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#define NUM_QUEENS 8

/* Returns 1 if the position is safe, else returns 0 */
int is_safe(int positions[][], int col, int row);
/* uses printf to print the chess board, returns nothing */
void print_board(int positions[][]);

int main() {
	int positions[NUM_QUEENS][NUM_QUEENS = { 0 }; /* array to store positions*/
	int moves = 0; /* using int instead of long is just wishful thinking 	 */
	int queensDone = 0; /* variable for our while-loop 						 */
	int lastIndex = 0;
	while (queensDone < NUM_QUEENS) {
		while (lastIndex < NUM_QUEENS) {
			if (is_safe(positions, queensDone, lastIndex)) {
				queensDone++; lastIndex = 0;
			} else {
				lastIndex++;
			}
		}
		/* We decrement queensDone IF this is not the FIRST or the LAST queen
		 * since we've not found a safe pos. */
		if (queensDone > 0 && queensDone < NUM_QUEENS)
			queensDone--;
		}
		
	}
	print_board(positions);
	printf("Finally. That only too %d moves", moves);
	
	return EXIT_SUCCESS;
}

