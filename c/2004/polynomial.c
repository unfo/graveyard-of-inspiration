/***************************************************************************
 *   Copyright (C) 2004 by Jan Wikholm                                     *
 *   jw@jw.fi                                                              *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif
#define DEBUG
#include <stdio.h>
#include <stdlib.h>

void printPolynome(int*, int count);

int main(int argc, char *argv[])
{
	int exponents[2] = { 0 };
	printf("Hello, world!\nThis is a simple polynomial calculator.\n");

	do {
		printf("Please enter the highest exponent of polynome 1: ");
		scanf("%d", &exponents[0]);
	} while (exponents[0] < 1);
#ifdef DEBUG
	printf("Exponents[0] = %d\n", exponents[0]);
#endif
	do {
		printf("Please enter the highest exponent of polynome 2: ");
		scanf("%d", &exponents[1]);
	} while (exponents[1] < 1);
#ifdef DEBUG
	printf("Exponents[1] = %d\n", exponents[1]);
#endif
	
	/* pointer arrays for the coeffients of the x^n */
	int* ppolynome[2] = { NULL };
	int i = 0;
	int j = 0;
	ppolynome[0] = malloc((exponents[0]+1) * sizeof(int));
	ppolynome[1] = malloc((exponents[1]+1) * sizeof(int));
	
	if (ppolynome[0] == NULL || ppolynome[1] == NULL) {
		printf("Nullpointer!\n");
		return EXIT_FAILURE;
	}
	for (j = 0; j < 2; j++) {
		printf("Polynome %d:\n", j+1);
		printf("Enter constant term: ");
		scanf("%d", ppolynome[j]);
		#ifdef DEBUG
		printf("Constant was: %d\n", *ppolynome[j]);
		#endif
		for (i = 1; i <= exponents[j]; i++) {
			printf("Coefficient for x^%d: ", i);
			scanf("%d", (ppolynome[j]+i));
		}
	}
	printPolynome(ppolynome[0], exponents[0]);
	printPolynome(ppolynome[1], exponents[1]);
	free(ppolynome[0]);
	free(ppolynome[1]);
	getchar();
	return EXIT_SUCCESS;
}

void printPolynome(int* pint, int count) {
	int exponent = count;
	for (; exponent > 0; exponent--) {
		if (pint[exponent] > 0)
			printf("(%dx^%d) + ", pint[exponent], exponent);
	}
	printf("%d\n", *pint);
}
