NUM = Integer(ARGV.shift || 1)
t = Time.now
count = i = j = 0
target = 317584931803
limit = Math.sqrt(target).to_i
flags0 = Array.new(limit,1)
known_primes = Array.new
sum = 0
NUM.times do
    count = 0
    flags = flags0.dup
    for i in 2 .. limit
      next unless flags[i]
      # remove all multiples of prime: i
      (i*i).step(limit, i) do |j|
        flags[j] = nil
      end
      known_primes << i
    end
end
e = Time.now
puts known_primes.sort.last
factors = Array.new
while target > 1 and not known_primes.include? target
  known_primes.each do |prime|
    if target % prime == 0
      puts '%d %% %d == 0 | target = %d' % [target, prime, (target/prime).to_i]
      factors << prime
      target /= prime
      next
    end
  end
end
factors << target
puts factors.sort.last

puts "Elapsed time: %.2f" % (e-t)

