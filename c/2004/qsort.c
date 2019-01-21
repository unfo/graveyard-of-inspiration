
   //-----------------------------------------------------------------------------------------
   // 3.3 How to make a callback in C by the means of the sort function qsort
   
   #include <stdlib.h>        // due to:  qsort
   #include <time.h>          //          randomize
   #include <stdio.h>         //          printf
   
   // comparison-function for the sort-algorithm
   // two items are taken by void-pointer, converted and compared
   int CmpFunc(const void* _a, const void* _b)
   {
	   // you've got to explicitly cast to the correct type
	   const int* a = (const int*) _a;
	   const int* b = (const int*) _b;
	   
	   if(*a > *b) return 1;              // first item is bigger than the second one -> return 1
	   else
		   if(*a == *b) return  0;         // equality -> return 0
	   else         return -1;         // second item is bigger than the first one -> return -1
   }
   
   
   // example for the use of qsort()
   int main()
   {
	   int field[100];
	   int c = 0;
	   srand(time(NULL));                     // initialize random-number-generator
	   for(c=0;c<100;c++)             // randomize all elements of the field
		   field[c]=random();
	   
	   // sort using qsort()
	   qsort((void*) field, /*number of items*/ 100, /*size of an item*/ sizeof(field[0]),
	   /*comparison-function*/ CmpFunc);
	   
	   // display first ten elements of the sorted field
	   printf("The first ten elements of the sorted field are ...\n");
	   for(c=0;c<10;c++)
		   printf("element #%d contains %.0d\n", c+1, field[c]);
	   printf("\n");
	   return 0;
   }
