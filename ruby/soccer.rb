#! /usr/local/bin/ruby
require 'dry.rb'
filename = 'K4Soccer.txt'
regexp = /^\s+\d{1,2}\. (.*?)\s+(\d+\s+){4}\s+(\d{2})\s+-\s+(\d{2})/
label_col = 1
col_one = 3
col_two = 4
puts smallestDifference(filename, regexp, label_col, col_one, col_two)
