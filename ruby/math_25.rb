last = runner = 1
order = 2
while last.to_s.length < 1000
  last, runner = (last+runner), last
  order += 1
end
puts order
