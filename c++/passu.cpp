//
// passu.cpp
// pasi majuri
//
#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <ctime>

using namespace std;

int annaluku(int range){
   int random_integer = 101 + (int) (32.0 * rand()/(RAND_MAX+1));
    return random_integer;
}    

void annamerkki(void){   
    int inluku;    
    int range = 26;

    int merkkimaara = 1;
  
    
    do {
    cout << "\nanna 0 lopettaaksesi\n";
    cin >> inluku;
    
    if(inluku == 0){
        break;
    }    
    cout << "\ntulos on : " ;
    for(int x=0;x<merkkimaara;x++){
        
        
      
        char pmerkki = (int) annaluku(range);
        cout << "tuli >"<<pmerkki<<"<..";
        printf("%o",pmerkki);

        
        
    }    
    
    
    } while (inluku !=0);

    return;  
}    





int main(int nNumberofArgs, char* pszArgs[])
{
    
    srand(time(NULL));
    cout << "\nArvotaan luku.\n";

    annamerkki();
 
    //system("PAUSE");
    return 0; 
}
