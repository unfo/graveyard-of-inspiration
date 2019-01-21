
-module(fibonacci).
-vsn(1).
-author({jan,wikholm}).
-purpose("to use with project euler problems").
-export([fib/1, start/0]).

start()  -> 
	Tab = ets:new(fibocache, [set]),
	ets:insert(Tab, [{0, 1}, {1,1}]),
	ets:tab2file(Tab, "fibocache.tab"),
	ets:delete(Tab).

fib(N) ->
	{ok, Tab} = ets:file2tab("fibocache.tab"),
	case ets:lookup(Tab, N) of
		[{_Number, Value}] -> Value;
		[] -> 
			NewValue = fib(N-2) + fib(N-1),
			ets:insert(Tab, [{N, NewValue}]),
			ets:tab2file(Tab, "fibocache.tab"),
			ets:delete(Tab),
			NewValue
	end.