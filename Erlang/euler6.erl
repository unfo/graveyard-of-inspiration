%% Problem 6
%% 30 November 2001
%% 
%% The sum of the squares of the first ten natural numbers is,
%% 1² + 2² + ... + 10² = 385
%% 
%% The square of the sum of the first ten natural numbers is,
%% (1 + 2 + ... + 10)² = 55² = 3025
%% 
%% Hence the difference between the sum of the squares of the first ten natural numbers and the square of the sum is 3025 385 = 2640.
%% 
%% Find the difference between the sum of the squares of the first one hundred natural numbers and the square of the sum.
%% Answer:
%% 25164150

-module(euler6).
-export([solve/1]).

solve(N) ->
	Numbers = lists:seq(1,N),
	SumOfNumbers = lists:sum(Numbers),
	SquareOfSumOfNumbers = SumOfNumbers * SumOfNumbers,
	Squares = [ X*X || X <- Numbers ],
	SquareOfSumOfNumbers - lists:sum(Squares).
	