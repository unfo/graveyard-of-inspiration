require 'fibonacci.rb'

fb = Fibonacci.new(1,1)
i = 3
until fb.is_dually_pandigital?
  fb.next!
  i = i.next
  puts "%d (%s) %s" % [i, fb.to_s.length, fb.to_s]
end
puts "\n\n\n-----\n%s - %s" % [i, fb.to_s]
