%% Problem 2
%% 19 October 2001
%% 
%% Each new term in the Fibonacci sequence is generated by adding the previous two terms. By starting with 1 and 2, the first 10 terms will be:
%%
%% 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...
%% 
%% Find the sum of all the even-valued terms in the sequence which do not exceed one million.
%%Answer:
%% 1089154
-module(euler_2).
-export([solve/1]).
-import(fibonacci, [fib/1]).

solve(N) ->
	FibonaccisLessOrEqualToOneMillion = [ fibonacci:fib(X) || X <- lists:seq(1,30)],
	EvenFibonaccis = [ Y || Y <- FibonaccisLessOrEqualToOneMillion, Y =< N, Y rem 2 =:= 0],
	io:format("Result: ~p~n", [lists:sum(EvenFibonaccis)]).