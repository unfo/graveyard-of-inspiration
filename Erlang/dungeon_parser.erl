-module(dungeon_parser).
-export([parse/0]).


parse() -> 
	io:format("Parsing..."),
	{ok, Dungeonfile} = file:open("dungeon.log", read),
	Dungeons = parse(Dungeonfile, 0),
	io:format("Total of ~p dungeons parsed~n", [Dungeons]),
	io:format("Done parsing."),
	file:close(Dungeonfile).

parse(Dungeonfile, DungeonsSoFar) ->
	Result = io:get_line(Dungeonfile, ''),
	case Result of
		eof ->
			DungeonsSoFar;
		_ ->
			NewDungeons = DungeonsSoFar + 1,
			%%io:format("Found: ~p~n", [Result]),
			ListOfMatches = regexp:matches(Result, "(-?[0-9]+);"),
			case ListOfMatches of
				{match, List} -> 
					ListOfStrings = lists:map(fun(X) -> mysub(X, Result) end, List),
					io:format("Dungeon values on line: ~p~n", [ListOfStrings]);
				_ -> io:format("Not found")
			end,
			parse(Dungeonfile, NewDungeons)
	end.
mysub({Start, Length}, String) ->
	ProperLength = Length - 1,
	list_to_integer(string:substr(String, Start, ProperLength));
mysub(_, _String) -> "".