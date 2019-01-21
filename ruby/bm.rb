require 'benchmark'
myHash = { "word" => "another" }
n = 1_000_000
Benchmark.bm(7) do |x|
  x.report("string")	{ for i in 1..n ; a = myHash["word"]; end }
  x.report("symbol")	{ for i in 1..n ; a = myHash[:word]; end }
end
