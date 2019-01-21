#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include <ctype.h>

/*
11-6-2014: 05:18 AM to 06:00 AM -- code review
11-9-2014: 08:52 AM to 09:15 AM -- food
11-8-2014: 07:00 PM to 08:05 PM -- meeting
11-8-2014: 05:30 PM to 06:36 PM -- personal appointment
11-6-2014: 02:47 PM to 03:23 PM -- work
11-11-2014: 07:14 AM to 08:32 AM -- meeting
11-11-2014: 11:22 AM to 12:10 PM -- code review
11-8-2014: 01:39 PM to 02:06 PM -- food
*/

/*
2014-11-06
    05:18 to 06:00 : code review
    06:16 to 07:32 : food
    07:51 to 08:25 : personal appointment
    08:53 to 09:55 : workout
    10:32 to 11:22 : sales call
    11:29 to 12:18 : code review
    12:18 to 13:30 : REDDIT!
    13:30 to 13:59 : sales call
    14:47 to 15:23 : work
    16:22 to 17:05 : code review
    17:54 to 18:17 : personal appointment
*/

/*
02:47 AM (167) to 03:23 PM (923) -- work
05:18 AM (318) to 06:00 AM (360) -- code review
06:16 AM (376) to 07:32 AM (452) -- food
07:51 AM (471) to 08:25 AM (505) -- personal appointment
08:53 AM (533) to 09:55 AM (595) -- workout
10:32 AM (632) to 11:22 AM (682) -- sales call
11:29 AM (689) to 12:18 AM (738) -- code review
01:30 PM (810) to 01:59 PM (839) -- sales call
04:22 PM (982) to 05:05 PM (1025) -- code review
05:54 PM (1074) to 06:17 PM (1097) -- personal appointment
*/


typedef struct Task {
	char name[64];
	unsigned int start_mins;
	unsigned int end_mins;
	unsigned int duration;
	struct Task* next;
} Task;

typedef struct {
	char date[11];
	unsigned int task_count;
	Task* first_task;
} Day;

int parse_timestamp(const char* str, const int start);
char* print_timestamp(const int minutes);

int main(int argc, char** argv)
{
	if (argc != 2) {
		printf("Use: %s <inputfile>\n", argv[0]);
		exit(-1);
	}

	FILE* schedule = fopen(argv[1], "r");
	if (!schedule || feof(schedule) || ferror(schedule)) {
		printf("Unable to open file %s\n", argv[1]);
		exit(-1);
	}

	unsigned int day_count = 0;
	Day* days = NULL;

	// int task_num = 0;

	while (!feof(schedule) && !ferror(schedule)) {
		char* date = calloc(sizeof(char), 11); // mm-dd-yyyy0
		char* buffer = calloc(sizeof(char), 1024);
	    if (buffer == NULL || date == NULL) {
	    	printf("Malloc problem!\n");
	    	free(date);
	    	free(buffer);
	        break;
	    }

	    fgets(buffer, 1024, schedule);

	    int buf_len = strlen(buffer);
	    if (buf_len == 0) {
	    	free(date);
	    	free(buffer);
	    	break;
	    }
		// printf("Got new line: %s\n", buffer);
		int date_length = 0;
		char chr = buffer[0];
		//          0123456789 123456789 1234567
		// 11-8-2014: 01:39 PM to 02:06 PM -- food
		// 0123456789date_length
		while (chr != ':' && date_length <= 11) {
			if (chr == '\0') {
				printf("Surprising end of string encountered at pos=%d/%d!\n", date_length, buf_len);
				break;
			}
			chr = buffer[++date_length];
		}

		strncpy(date, buffer, date_length);
		date[date_length+1] = '\0';

		unsigned int task_start_mins = parse_timestamp(buffer, date_length+2);
		unsigned int task_end_mins =   parse_timestamp(buffer, date_length+14);
		unsigned int task_name_start = date_length+26;
		unsigned int task_name_len = (strlen(buffer) - task_name_start) + 1;
		
		char* tsk_nm = calloc(sizeof(char), task_name_len);
		assert(tsk_nm);
		
		for (unsigned int c = 0; c < task_name_len; c++) {
			if (buffer[task_name_start+c] == '\n') {
				tsk_nm[c] = '\0';
				break;
			} else {
				tsk_nm[c] = buffer[task_name_start + c];
			}
		}

		free(buffer);
		
		Day* day = NULL;

		for (unsigned int i = 0; i < day_count; i++) {
			if (date == NULL || days[i].date == NULL)
				continue;

			int strcmp_res = strcmp(days[i].date, date);
			if (strcmp_res == 0) {
				
				day = &days[i];
				break;
			} 
		}

		if (day == NULL) {
			day_count++;
			if (day_count == 1) {
				days = malloc(sizeof(Day));
			} else {
				size_t ds = sizeof(Day);
				ds *= day_count;
				days = realloc(days, ds);
			}
			
			assert(days);
			day = &days[day_count-1];
			day->task_count = 0;
			strncpy(day->date, date, 11);
		}

		Task* task = NULL;

		day->task_count++;
		if (day->task_count == 1) {
			day->first_task = malloc(sizeof(Task));
			if (!day->first_task) {
				printf("Unable to allocate Task\n");
				exit(-1);
			}
			task = day->first_task;
		} else {
			//day->tasks = realloc(day->tasks, (day->task_count) * sizeof(Task));
			task = day->first_task;
			while (task->next)
				task = task->next;

			// printf("Last one: %s\n", task->name);
			task->next = malloc(sizeof(Task));
			task = task->next;
		}

		if (task == NULL) {
			printf("Task is null!\n");
			exit(-1);
		}
		strcpy(task->name, tsk_nm);
		free(tsk_nm);
		task->start_mins = task_start_mins;
		task->end_mins = task_end_mins;
		task->duration = task_end_mins - task_start_mins;
		task->next = NULL;
		printf("Day: date=%s >> Task: name=[%s] start=%d end=%d duration=%d\n", day->date, task->name, task->start_mins, task->end_mins, task->duration);
		free(date);
	}

	for (unsigned int d = 0; d < day_count; d++) {
		// printf("Processing date: %s\n", days[d].date);
		Task* base = days[d].first_task;
		Task* base_prev = NULL;

		while (base != NULL) {
			Task* smallest = base;
			Task* smallest_prev = base_prev;
			Task* current = base;
			Task* prev = base_prev;
			printf("sorting %s to %s -- %s\n", print_timestamp(base->start_mins), print_timestamp(base->end_mins), base->name);

			while (current != NULL) {
				if (current->start_mins < smallest->start_mins) {
					// printf("\t this is smaller! %s to %s -- %s\n", print_timestamp(base->start_mins), print_timestamp(base->end_mins), base->name);

					smallest = current;
					smallest_prev = prev;
				}
				prev = current;
				current = current->next;
			}
			if (smallest != base) {
				smallest_prev->next = base;
				Task* nnext = base->next;
				base->next = smallest->next;
				smallest->next = nnext;
				if (base_prev == NULL) {
					days[d].first_task = smallest;
				} else {
					base_prev->next = smallest;
				}
			}
			base = smallest->next;
			base_prev = smallest;
		}

		// printf("Days tasks are now in order\n");
		base = days[d].first_task;
		int freetime = 0;
		unsigned int start_free = 0;
		unsigned int end_free = 0;
		while (base != NULL) {
			printf("%s (%d) to %s (%d) -- %s\n", 
				print_timestamp(base->start_mins), 
				base->start_mins, 
				print_timestamp(base->end_mins),
				base->end_mins,
				base->name);


			if (base->next != NULL ) {
				unsigned int fstart = base->end_mins;
				Task* endtask = base->next;
				unsigned int fend = endtask->start_mins;
				int ftime = fend - fstart;
				// printf("Free time to next slot: %d - %d = %d --> %s\n", fend, fstart, ftime, print_timestamp(fend));
				if (ftime > freetime) {
					freetime = ftime;
					start_free = fstart;
					end_free = fend;
				}
				Task* next = base->next;
				free(base);
				base = next;
			} else {
				free(base);
				base = NULL;
			}
			// printf("\n");
		}
		free(base);

		printf("Biggest free time for reddit on %s is from %s to %s - total of %d minutes\n", 
			days[d].date, print_timestamp(start_free), print_timestamp(end_free), freetime);

		// printf("Day's tasks are free now\n");
	}
	free(days);
	fclose(schedule);

	// printf("All done\n");
	return 0;
}

/// 01:39 PM
/// => PM = +12h
/// + 1h = 13 * 60 min
/// + 39 min
int parse_timestamp(const char* str, const int start) {

	int h, m;
	char ampm;
	char* time_str = calloc(sizeof(char), 8);
	sscanf(&str[start], "%d:%d %c", &h, &m, &ampm);
	sscanf(&str[start], "%7s", time_str);
	// int hours = (h_tens * 10) + h;
	// int mins = (m_tens * 10) + m;


	if (h < 12 && ampm == 'P')
		h += 12;

	// printf("%s => %d:%d\n", time_str, h, m);
	m += (h * 60);
	
	return m;
}

char* print_timestamp(const int minutes) 
{
	int hours = minutes / 60;
	int mins = minutes % 60;
	char* ampm = "AM";
	if (hours > 12) {
		hours = hours - 12;
		ampm = "PM";
	}

	char* str = calloc(sizeof(char), 9); // hh:mm AM0
	sprintf(str, "%02d:%02d %s", hours, mins, ampm);

	return str;
}
