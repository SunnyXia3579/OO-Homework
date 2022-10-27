#include <stdio.h>
#include <string.h>

int main() {
	FILE *in = fopen("stdin.txt", "r");
	char line[100][100];
	int i, j;
	
	for (i = 0;fgets(line[i], 100, in) != NULL;i++) {
		if (strstr(line[i], "ADD") != NULL) {
			if (strstr(line[i], "floor") != NULL && !(line[i][strlen(line[i]) - 2] == '1' && line[i][strlen(line[i]) - 3] == '3')) {
				int len = strlen(line[i]) - 1;
				line[i][len] = '\0';
				strcat(line[i], "-6-0.2-31\n");
			} else if (strstr(line[i], "building") != NULL && line[i][strlen(line[i] - 3)] != '.') {
				int len = strlen(line[i]) - 1;
				line[i][len] = '\0';
				strcat(line[i], "-6-0.4\n");
			}
		}
	}
	fclose(in);
	in = fopen("stdin.txt", "w");
	for (j = 0;j < i;j++) {
		fprintf(in, "%s", line[j]);
	}
	return 0;
} 
