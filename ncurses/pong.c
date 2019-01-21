#include <ncurses.h>


WINDOW *create_newwin(int height, int width, int starty, int startx);
void destroy_win(WINDOW *local_win);

int main(int argc, char *argv[])
{WINDOW *my_win;
	int startx, starty, width, height, row, col;
	int ch;
	
	initscr();/* Start curses mode */
	if(has_colors() == FALSE)
	{endwin();
		printf("You terminal does not support color\n");
		exit(1);
	}
	start_color();/* Start color */
	init_pair(1, COLOR_RED, COLOR_BLACK);
	cbreak();/* Line buffering disabled, Pass on everything to me */
	keypad(stdscr, TRUE);/* I need that nifty F1 */
	timeout(4);
	height = 3;
	width = 10;
	getmaxyx(stdscr,row,col);
	starty = (LINES - height) / 2;/* Calculating for a center placement */
	startx = 1;/* of the window*/
	printw("Press F1 to exit");
	refresh();
	while((ch = getch()) != KEY_F(1) && (starty < row)) 
	{
		while(startx < col-1) {
			mvprintw(starty, startx, " ");
			mvprintw(starty, ++startx, "->");
			mvprintw(0,0, "Current coords: %d,%d", startx, starty);
			refresh();
			getch();
		}
		mvprintw(starty++, startx, "  "); /* just to clear that one char */
		while(startx > 1) {
			mvprintw(starty, startx, "  ");
			mvprintw(starty, --startx, "<-");
			mvprintw(0,0, "Current coords: %d,%d", startx, starty);
			refresh();
			getch();
		}
		mvprintw(starty++, startx, "  ");
	}
	getch();
	endwin();/* End curses mode  */
	return 0;
}

WINDOW *create_newwin(int height, int width, int starty, int startx)
{WINDOW *local_win;
	
	local_win = newwin(height, width, starty, startx);
	box(local_win, 0 , 0);/* 0, 0 gives default characters 
	* for the vertical and horizontal
	* lines*/
	wrefresh(local_win);/* Show that box */
	
	return local_win;
}

void destroy_win(WINDOW *local_win)
{
	/* box(local_win, ' ', ' '); : This won't produce the desired
	* result of erasing the window. It will leave it's four corners 
	* and so an ugly remnant of window. 
	*/
	wborder(local_win, ' ', ' ', ' ',' ',' ',' ',' ',' ');
	/* The parameters taken are 
	* 1. win: the window on which to operate
	* 2. ls: character to be used for the left side of the window 
	* 3. rs: character to be used for the right side of the window 
	* 4. ts: character to be used for the top side of the window 
	* 5. bs: character to be used for the bottom side of the window 
	* 6. tl: character to be used for the top left corner of the window 
	* 7. tr: character to be used for the top right corner of the window 
	* 8. bl: character to be used for the bottom left corner of the window 
	* 9. br: character to be used for the bottom right corner of the window
	*/
	wrefresh(local_win);
	delwin(local_win);
}
