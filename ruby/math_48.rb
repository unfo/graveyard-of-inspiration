sum = 0
1.upto(1000) do |number|
  sum += number**number
end
puts sum
puts sum.to_s[-10,10]
