sum = 0
numbers = (1...1000).collect { |number| sum += number if number % 3 == 0 || number % 5 == 0 }
puts sum
