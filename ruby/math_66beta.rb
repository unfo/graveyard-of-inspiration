

highest_x = 0
index_of_highest = 0
1.upto(1000) do |d|
  next if (Math.sqrt(d) == Math.sqrt(d).to_i or d == 61) 
  x,y = 0,0
  until (x**2 == d*y**2 + 1)
    y = y.next
    x = Math.sqrt(d*y**2 + 1)
    if x.to_i == x
      highest_x = [highest_x, x.to_i].max
      index_of_highest = d if x.to_i == highest_x
      puts "D(%d) -> x(%d) -> y(%d)" % [d, x.to_i, y]
    end
    x = x.to_i
  end
end
puts highest_x
