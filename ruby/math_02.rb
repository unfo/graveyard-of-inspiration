square_sum = 0
sum_square = 0
1.upto(100) { |number| 
  square_sum += number ** 2
  sum_square += number
}
sum_square = sum_square ** 2
puts (square_sum-sum_square).abs
