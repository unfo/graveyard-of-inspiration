-module(jw).
-export([upto/2]).

%% Throw out non-integers
upto({from, X}, {to, Y}) when not is_integer(X); not is_integer(Y) -> throw(fromOrToWereNotIntegers);
%% From must be lower or equal than to
upto({from, X}, {to, Y}) when X > Y -> throw(fromHigherThanTo);
%% If From == To then we have can just send the element back
upto(From, From) -> [From];
%% Otherwise we need to start building a list
upto(From, To) -> lists:reverse(upto_with_list(From, To, [])).


upto_with_list({from, X}, {to, X}, List) -> [X|List];
upto_with_list({from, X}, {to, Y}, List) -> upto_with_list({from, X + 1}, {to, Y}, [X|List]).
