#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 

void error(const char *msg)
{
    perror(msg);
    exit(0);
}

int main(void)
{
    int sockfd, portno, n;
    n = 1;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[256];
    // if (argc < 3) {
       // fprintf(stderr,"usage %s hostname port\n", argv[0]);
       // exit(0);
    // }
    portno = 5842; //atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *) &serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");

 
    long sum = 0;
    long hsum = 0;

    for (int i = 0; i < 4; i++ ){
        unsigned int num;
        if (recv(sockfd, (unsigned int *)&num, sizeof(unsigned int), 0) < (int)sizeof(unsigned int)) {
            error("ERROR reading from socket");
        }
        unsigned int converted = ntohl(num);
        hsum += num;
        // num = ntohl(num);
        sum += converted;
        printf("%d. got %15u ntohl = %15u -- sum = %20ld (hsum = %20ld)\n", i, num, converted, sum, hsum);

    }

    sum = htonl(sum);

    // printf("sum after htonl = %ld\n", sum);
    n = send(sockfd, &hsum, sizeof(uint32_t), 0);
    if (n < 0) 
        error("ERROR writin to socket");

/*
    int a,b,c,d;
    printf("Reading 4 unsigned ints: ");
    n = sscanf(buffer,"%d%d%d%d", &a, &b, &c, &d);
    if (n < 0) 
        error("ERROR reading from socket");

    unsigned int sum = a+b+c+d;
    printf("%d + %d + %d + %d = %d\n", a,b,c,d,sum);


    // printf("%s\n",buffer);
    n = write(sockfd, &sum, sizeof(sum));
    if (n < 0) 
        error("ERROR writing to socket");

    */
    bzero(buffer,256);
    n = read(sockfd,buffer,255);
    if (n < 0) 
        error("ERROR reading from socket");

    printf("Final result: %s\n", buffer);
    close(sockfd);
    return 0;
}
