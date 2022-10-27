#include <stdio.h>
#include <string.h>


int main() {
	FILE *inf = fopen("result.txt", "r");
	char line[100] = {0};
	int i;
	char c[100];
	int out[61] = {0}, in[61] = {0};
	int num;
	int line_num;
	
//	while(~scanf("%s", c)) {
//	scanf("%d", &line_num);

		scanf("%s", c);
		fseek(inf, 0, SEEK_SET);
		i = 1;
		for (fgets(line, 100, inf);fgets(line, 100, inf) != NULL;i++) {
			if (strstr(line, c) != NULL) {
				printf("%s", line);
			}
//			if (i == line_num) {
//				printf("\n%s\n", line);
//			}
		}	
//			for (i = 0;line[i] != '-' && line[i] != '\0';i++);
//			i++;
//			if (line[i] >= '0' && line[i] <= '9') {
//				while (line[i] >= '0' && line[i] <= '9') {
//					i++;
//				}
//				i++;
//			}
//			if (line[11] == 'O' && line[12] == 'U') {
//				printf("%s", line);
//				for (i = 15, num = 0;line[i] >= '0' && line[i] <= '9';i++) {
//					num = 10 * num + line[i] - '0';
//				}
//				out[num] = 1;
//			}
//			if (line[11] == 'I' && line[12] == 'N') {
//				printf("%s", line);
//				for (i = 15, num = 0;line[i] >= '0' && line[i] <= '9';i++) {
//					num = 10 * num + line[i] - '0';
//				}
//				in[num] = 1;
//			}
//		}
//		for (i = 1;i <= 60;i++) {
//			if (out[i] == 0) {
//				printf("missing out passenger %d\n", i);
//			}
//			if (in[i] == 0) {
//				printf("missing in passenger %d\n", i);
//			}
//		}
//	}
	
//	FILE *test = fopen("stdin.txt", "w");
//	for(i = 0;i < 100;i++) {
//		fprintf(test, "[0.0]%d-FROM-A-1-TO-A-10\n", i);
//	}
	
	return 0;
}
