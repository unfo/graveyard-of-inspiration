#! /usr/local/bin/ruby
require 'dry.rb'
filename = 'K4Weather.txt'
regexp = /^\s+(\d{1,2})\s+(\d{2})\s+(\d{2})/
label_col = 1
col_one = 2
col_two = 3
puts smallestDifference(filename, regexp, label_col, col_one, col_two)
