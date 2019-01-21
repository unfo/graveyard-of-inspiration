%% If we list all the natural numbers below 10 that are multiples of 3 or 5, 
%% we get 3, 5, 6 and 9. The sum of these multiples is 23.
%% Find the sum of all the multiples of 3 or 5 below 1000.
%%
%% Original url: http://projecteuler.net/index.php?section=problems&id=1

-module(euler_1).
-export([problem1/1]).

problem1(N) ->
	MultiplesOfThreeOrFive = [ X || X <- lists:seq(1,N), (X rem 3 =:= 0) or (X rem 5 =:= 0) ],
	io:format("Result: ~p~n", [lists:sum(MultiplesOfThreeOrFive)]).
	
%% 
%% euler_1:problem1(999).
%% Result: 233168
%% ok
%% -- This is correct.aa