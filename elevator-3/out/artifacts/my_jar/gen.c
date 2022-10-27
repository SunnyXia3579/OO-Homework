#include <stdio.h>
#include <stdlib.h>

#define max(a, b) (((a) > (b)) ? (a) : (b))

const int MAX = 69;

int i;
double time_stamp = 1.0;
char building, b;
int from;
int to;
int floor;

int e_id = 5;

int axis[11] = {0};
int open = 0;
int ele_cnt = 5; 

int main() {
	srand(time(NULL));
	
	printf("[69.0]ADD-floor-6-10\n");
	for (i = 1;i < 70;i++) {
		char b1 = rand() % 5 + 'A';
		char b2 = rand() % 5 + 'A';
		while (b1 == b2) {
			b2 = rand() % 5 + 'A';
		}
		printf("[70.0]%d-FROM-%c-10-TO-%c-10\n", i, b1, b2);
	}
	return 0;
	
	for (i = 0;i < MAX;i++) {
		time_stamp += rand() % 100 / 100.0;
		int op = rand() % 4;
		if (op == 0 && open) {
			building = rand() % 5 + 'A';
			b = building = rand() % 5 + 'A';
			while (building == b) {
				b = rand() % 5 + 'A';
			}
			floor = rand() % 10 + 1;
			while (axis[floor] == 0) {
				floor = rand() % 10 + 1;
			}
			printf("[%.1f]%d-FROM-%c-%d-TO-%c-%d\n", time_stamp, i + 1, building, floor, b, floor);
			continue;
		}
		else if (op == 1){
			building = rand() % 5 + 'A';
			from = rand() % 10 + 1;
			to = rand() % 10 + 1;
			if (from == to) {
				if (from == 10) {
					to = rand() % 9 + 1;
				} else if (from == 1) {
					to = rand() % 9 + 2;
				} else {
					to = from + 1;
				}
			}
			printf("[%.1f]%d-FROM-%c-%d-TO-%c-%d\n", time_stamp, i + 1, building, from, building, to);
		} else if (op == 2) {
			if (ele_cnt >= 15) {
				i--;
				time_stamp -= 0.1;
				continue;
			}
			ele_cnt++;
			open = 1;
			e_id++;
			floor = rand() % 10 + 1;
			axis[floor] = 1;
			printf("[%.1f]ADD-floor-%d-%d\n", time_stamp, e_id, floor);
			time_stamp++;
		} else {
			if (ele_cnt >= 15) {
				i--;
				time_stamp -= 0.1;
				continue;
			}
			ele_cnt++;
			e_id++;
			building = rand() % 5 + 'A';
			printf("[%.1f]ADD-building-%d-%c\n", time_stamp, e_id, building);
			time_stamp++;
		}
	}
	
	return 0;
}
