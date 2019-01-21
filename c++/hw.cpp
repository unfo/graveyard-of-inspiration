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

#include <iostream>
#include <cstdlib>
#include <string>

#define MIN_DAY 1
#define MAX_DAY 31
#define MIN_MONTH 1
#define MAX_MONTH 12
#define MIN_YEAR 1900
#define MAX_YEAR 2004

using namespace std;

bool checkDay(short int& day);
bool checkMonth(short int& month);
bool checkYear(short int& year);
string suffix(short int& day);
string nameOfMonth(short int& month);

int main(int argc, char *argv[]) {
	short int day = 0;
	short int month = 0;
	short int year = 0;
    
	cout << "Enter your birthday" << endl;
    
	do {
		cout << "Day: ";	// Ask for day ..
		cin >> day;
	} while (!checkDay(day));		// ... until checkDay returns true
    
	do {
		cout << "Month: ";
		cin >> month;
	} while (!checkMonth(month));
    
	do {
		cout << "Year: ";
		cin >> year;
	} while (!checkYear(year));
    
	cout 	<< "You were born on " 
		<< day << suffix(day) << " "	// Print 1st, 2nd, 3rd, 4th etc
		<< nameOfMonth(month) << " "	// Print Jan, Feb, Mar etc
		<< year << endl;		// Print the year as numbers
       
    return EXIT_SUCCESS;
}

bool checkDay(short int& day) {
	return (day >= MIN_DAY && day <= MAX_DAY);
}

bool checkMonth(short int& month) {
	return (month >= MIN_MONTH && month <= MAX_MONTH);
}

bool checkYear(short int& year) {
	return (year >= MIN_YEAR && year <= MAX_YEAR);
}

string suffix(short int& day) {
	switch (day) {
		case 1:
		case 21:
		case 31: return "st"; break;
		case 2:
		case 22: return "nd"; break;
		case 3:
		case 23: return "rd"; break;
		default: return "th";
	}
}

string nameOfMonth(short int& month) {
	string names[] = {
				"Jan", "Feb", "Mar",
				"Apr", "May", "Jun",
				"Jul", "Aug", "Sep",
				"Oct", "Nov", "Dec"
			};
	return names[month-1];
}
