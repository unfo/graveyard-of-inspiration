 begin
 Thread.current.kill
 exit(1)
 #`kill -9 #{$$}` # this will successfully kill you
 puts "after kill" 
 ensure
 puts "HA HA!" 
 end

