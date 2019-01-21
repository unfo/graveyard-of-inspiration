require 'benchmark'
n = 1_000_000
Benchmark.bm(3) do |x|
  x.report("for in")	{ i = 0; for z in 1..n; i = i+1; end; }
  x.report("each")		{ i = 0; (1..n).each { |z| i = i+1 } }
	x.report("times")   { i = 0; n.times { i = i+1 } }
end
