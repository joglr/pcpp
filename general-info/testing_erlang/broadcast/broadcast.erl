-module(broadcast).
-export([start/0,person/0,broadcaster/1]).

person() ->
	receive
		{message,M} ->
			io:fwrite(M ++ "\n"),
			person()
	end.

broadcast([],_) -> true;
broadcast([Pid|L],M) -> 
	Pid ! {message,M},
	broadcast(L,M).

broadcaster(L) ->
	receive
		{subscribe,Pid} ->
			broadcaster([Pid|L]);
		{unsubscribe,Pid} ->
			broadcaster(lists:delete(Pid,L));
		{message,M} ->
			broadcast(L,M),
			broadcaster(L)
	end.

start() ->
	Broadcaster = spawn(broadcast,broadcaster,[[]]),
	P1 = spawn(broadcast,person,[]),
	P2 = spawn(broadcast,person,[]),
	P3 = spawn(broadcast,person,[]),
	Broadcaster ! {subscribe,P1},
	Broadcaster ! {subscribe,P2},
	Broadcaster ! {subscribe,P3},
	Broadcaster ! {message,"Purses half price!"},
	Broadcaster ! {unsubscribe,P2},
	Broadcaster ! {message,"Shoes half price!!"}.
