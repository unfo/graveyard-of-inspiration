require 'log4r'
include Log4r

@log = Logger.new 'log'
so = StdoutOutputter.new 'console'
@log.add(so)

def main
  i = 1
  start = 7
  numbers = Math.sqrt(317584931803).to_i
  @log.info 'numbers = %d' % numbers
  primes = get_primes(start, numbers, [2, 3, 5, 7])
  factors = get_factors(start, numbers, primes)
  puts factors.sort[0]
  puts factors.last
end

def get_primes(bottom, limit, known_primes)
  (bottom..limit).each {|number|
    next if number % 2 == 0 
    next if known_primes.include? number
    putc "."
    divisable = false
    known_primes.each {|prime| 
      break if (divisable = (number % prime == 0))
    }
    if not divisable
      known_primes << number
      printf('%d', number)
    end
  }
  return known_primes
  
end

def get_factors(bottom, limit, primes)
  all_results = Hash.new
  (bottom..limit).each { |number|
    result = Array.new
    key = number
    @log.debug 'Factors for: %d' % number
    while not primes.include? number
      primes.each {|prime|
        if number % prime == 0
          result << prime
          number /= prime
          @log.debug '%d' % prime
          break
        end
     }
    end
    result << number
    @log.debug " finally %d\n--" % number
    all_results[key] = result
  }
  return all_results
end
main
