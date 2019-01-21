/* Simple Blackjack with commandline interface */

#include<ncurses.h>
#include<stdlib.h>

/* Initialize the possible card labels */
char cards[] = "A23456789TJQK";
/* Initialize the values of cards */
int values[] = {
				1,2,3,4,5,6,7,8,9,
				10,10,10,10
				};

void show_cards(int hand[], int howMany);
int draw_card(void); /* draw cards with the ncurses lib */
int stay(char);
int main() {
	srand(time(NULL));
    initscr();
    cbreak();
    noecho();
    int rounds = 666;
    int round = 0;
    while (round < rounds) {
	int hand[5];
	int handValue = 0;
	int howMany = 0;
	char hitstay = 'h';
	int row,col;
	getmaxyx(stdscr,row,col);
	int dealerHand[5];
	int dealerHandValue = 0;
	int dealerHowmany = 0;
	
	while (howMany < 5 && handValue < 21 && !stay(hitstay)) {
		hand[howMany] = draw_card();
		if (hand[howMany] > 0)
			handValue += values[hand[howMany]];
		else
			if (handValue > 10)
				handValue++;
			else
				handValue += 11;
		howMany++;
		mvprintw(2,0,"Your cards:\n");
		show_cards(hand, howMany);
		printw("Total value: %d\n", handValue);
		if (handValue < 21 && howMany < 5) {
			printw("(H)it or (S)tay: ");
			hitstay = (char) getch();
		}
	}
	if (handValue > 21)
		mvprintw(row-2,0,"You're busted!");
	else {
	
		while (dealerHowmany < 5 && dealerHandValue < handValue) {
			dealerHand[dealerHowmany] = draw_card();
			if (dealerHand[dealerHowmany] > 0)
				dealerHandValue += values[dealerHand[dealerHowmany]];
			else
				if (dealerHandValue > 10)
					dealerHandValue++;
				else
					dealerHandValue += 11;
			dealerHowmany++;
			mvprintw(2,col/2,"Dealers cards:\n");
			int i;
			for (i = 0; i < dealerHowmany; i++)
				mvprintw(3,col/2+i*2,"%c ", cards[dealerHand[i]]);
			mvprintw(4,col/2,"Total value: %d\n", dealerHandValue);
		}
		if (handValue == 21 && dealerHandValue < 21)
			mvprintw(row-2,0,"You Win!");
		else {
			if (howMany == 5)
				mvprintw(row-2,0,"Five-card charlie! you win!");
			else {
				if (dealerHandValue > handValue && dealerHandValue <= 21)
					mvprintw(row-2,0,"You lose.");
				else if (dealerHandValue == handValue)
						mvprintw(row-2,0,"It's a push.");
				else
					mvprintw(row-2,0,"You win.");
			}
		}
	}
	getch();
	round++;
	mvprintw(row-2,0,"                                          ");
	}
	endwin();
	return EXIT_SUCCESS;
}

void show_cards(int hand[], int howMany) {
	int i;
	for (i = 0; i < howMany; i++)
		printw("%c ", cards[hand[i]]);
	printw("\n");
}
int draw_card() {
	return (int) (13.0*rand()/(RAND_MAX));
}
int stay(char hs) {
	return (hs == 'S' || hs == 's') ? 1 : 0;
}
