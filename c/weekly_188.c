#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stddef.h>
#include <ctype.h>

#define DEBUG 0
#define p(fmt, ...) if (DEBUG) { printf(fmt, __VA_ARGS__); }

/////////
///////// https://www.reddit.com/r/dailyprogrammer/comments/2lvgz6/20141110_challenge_188_easy_yyyymmdd/ //////
///////// /u/unfo
/////////
// iso 8601 standard for dates tells us the proper way to do an extended day is yyyy-mm-dd
// yyyy = year
// mm = month
// dd = day
// A company's database has become polluted with mixed date formats. They could be one of 6 different formats
// yyyy-mm-dd
// mm/dd/yy
// mm#yy#dd
// dd*mm*yyyy
// (month word) dd, yy
// (month word) dd, yyyy
// (month word) can be: Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec

char* rectify(char* probably_illegal_date);
int month_from_word(char** month_word);
char date_divider(const char* date_str);

int main(int argc, char** argv) 
{
	if (argc != 2) {
		printf("Use: %s <input file>\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	FILE* input = fopen(argv[1],"r");
	if (!input) {
		printf("Unable to open %s for reading\n", argv[1]);
		exit(EXIT_FAILURE);
	}

	char* line = NULL;

	while (!feof(input) && !ferror(input)) {
		line = calloc(sizeof(char), 128);
		if (!line) {
			printf("calloc failed\n");
			break;
		}


		fgets(line, 127, input);
		unsigned int len = 0;
		while (line[len] != '\0' && line[len] != '\n')
			len++;

		if (len < 8)  // yy mm dd minimum
			break;

		line[len] = '\0'; // ignore \n

		printf("%-15s ", line);
		p("Got line %s\n", line);
		printf("%s\n", rectify(line));
	}

	free(line);
	fclose(input);
}

// yyyy-mm-dd
// mm/dd/yy
// mm#yy#dd
// dd*mm*yyyy
// (month word) dd, yy
// (month word) dd, yyyy
// (month word) can be: Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec
char* rectify(char* probably_illegal_date) {
	if (probably_illegal_date == NULL)
		return probably_illegal_date;

	char* original_pointer = probably_illegal_date;
	short int year, month, day;
	char divider = date_divider(probably_illegal_date);
	switch (divider) {
		case '-':
			printf("%-15s", "yyyy-mm-dd");
			return original_pointer; // no need for further adjustments
			break;
		case '/':
			printf("%-15s", "mm/dd/yy");
			sscanf(probably_illegal_date, "%hd/%hd/%hd", &month, &day, &year);
			break;
		case '#':
			printf("%-15s", "mm#yy#dd");
			sscanf(probably_illegal_date, "%hd#%hd#%hd", &month, &year, &day);
			break;
		case '*':
			printf("%-15s", "dd*mm*yyyy");
			sscanf(probably_illegal_date, "%hd*%hd*%hd", &day, &month, &year);
			break;
		default:
			printf("%-15s", "Mon dd, yyYY");
			char* month_word = calloc(sizeof(char), 4);
			sscanf(probably_illegal_date, "%3s %hd, %hd", month_word, &day, &year);
			p("1Got month word pointer %p [%s]\n", month_word, month_word);
			p("2Got month word pointer %p [%s]\n", month_word, month_word);
			month = month_from_word(&month_word);
			free(month_word);
			break;
	}


	if (year < 100) { // '95 vs '05
		if (year < 49) { //  If it is yy then it is only the last 2 digits of the year. Years only go between 1950-2049.
			year += 2000;
		} else {
			year += 1900;
		}
	}
	p("Should be fixed: %d-%02d-%02d\n", year, month, day);
	sprintf(original_pointer, "%d-%02d-%02d", year, month, day);
	original_pointer[10] = '\0'; //yyyy-mm-dd = 4+1+2+1+2 = 10 chars;

	return original_pointer;
}

char date_divider(const char* date_str) {
	char divider = '\0';
	unsigned int i = 0;
	while (date_str[i] != '\0') { // for exercise not using strlen
		if (!isdigit(date_str[i])) {
			divider = date_str[i];
			break;
		}
		i++;
	}
	p("Divider for [%s] = %c\n", date_str, divider);
	return divider;
}

int month_from_word(char** month_pointer) {
	char* months[] = { 
						  "Jan","Feb","Mar","Apr",
						  "May","Jun","Jul","Aug",
						  "Sep","Oct","Nov","Dec"
					};
	int month = 0;
	char* month_word = *month_pointer;
	p("month from word ptr %p = %s\n", month_word, month_word);

	while (month <= 12) {
		int cmp = strcmp(month_word, months[month]);
		p("%s cmp %s = %d\n", month_word, months[month], cmp);
		
		if (cmp == 0) {
			break;
		}

		month++;
	}

	month++; // months are 1-indexed not 0
	return month;

}
