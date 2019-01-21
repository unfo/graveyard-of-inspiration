#include <ncurses.h>/* ncurses.h includes stdio.h */  
#include <string.h> 

WINDOW *create_newwin(int height, int width, int starty, int startx);
void destroy_win(WINDOW *local_win);


int main()
{
	WINDOW *my_win;
	int startx, starty, width, height;
	char msg1[]="Width: ";/* message to be appeared on the screen */
	char msg2[]="Height: ";
	char wid[10];
	char hei[10];
	int row,col;	/* to store the number of rows and *
					 * the number of colums of the screen */
	initscr();		/* start the curses mode */
	getmaxyx(stdscr,row,col);	/* get the number of rows and columns */
	mvprintw(2,2,"%s",msg1); /* print the message at the center of the screen */
	getstr(wid);
	width = atoi(wid);
	mvprintw(3,2,"%s",msg2);
	getstr(hei);
	height = atoi(hei);
	startx = 2;
	starty = 4;
	if (width < COLS-2 && height < LINES-4) {
		my_win = create_newwin(height, width, starty, startx);
		mvprintw(LINES - 2, 0, "Windowsize %d x %d with a total area of %d", width,height,width*height);
	} else {
		mvprintw(4,2, "Window too big");
	}
	getch();
	endwin();
	
	return 0;
}

WINDOW *create_newwin(int height, int width, int starty, int startx)
{
	WINDOW *local_win;
	
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
