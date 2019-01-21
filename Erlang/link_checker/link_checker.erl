-module(link_checker).
-author("Jan Wikholm").

-export([check/2]).

-record(linken, {
				url,
				status = unchecked,
				referrers = [],
				external = false
			   }).

%% The main outward functionality
check(Url, Output) ->
	io:format("Checking ~p and outputting it to ~p~n", [Url, Output]),
	RootLink = #linken{url=Url},
	NonProcessedLinks = [RootLink],
	ProcessedLinks = [],
	process_links(NonProcessedLinks, ProcessedLinks).

process_links([], List) ->
	io:format("Done processing~n"),
	lists:foreach(fun(X) -> pp_url(X) end, List);
process_links([H|T], List) ->
	process_links(T, [H] ++ List).
	
pp_url(X) when is_record(X, linken) ->
	#linken{url=Url, status=Status, referrers=Referrers, external=_External} = X,
	io:format("status=~p;url=~p;referers=~p~n", [Status, Url, lists:concat(Referrers)]).
