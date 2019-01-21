require 'log4r'
include Log4r

@log = Logger.new 'log'
so = StdoutOutputter.new 'console'
@log.add(so)

def main
  i = 1
  start = 2
  numbers = 20
  primes = get_primes(start, numbers, [2, 3])
  p primes
  factors = get_factors(start, numbers, primes)
  p factors.sort
  freq = get_frequencies(factors)
  @log.debug "Freq:\n"
  p freq
  final_result = 1
  freq.each_pair {|key, value| final_result *= key**value }
  puts final_result
end

def get_primes(bottom, limit, known_primes)
  (bottom..limit).each {|number|
    @log.debug 'Processing %d' % number
    divisable = false
    known_primes.each {|prime| 
      if number % prime == 0
        divisable = true
        @log.debug '%d %% %d == 0' % [number, prime]
      end
    }
    if not divisable
      known_primes << number
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
def get_frequencies(number_list)
  freq_result = Hash.new
 
  number_list.values.each {|number_array|
    freq = Hash.new
    number_array.each {|number|
      if freq[number]
        freq[number] += 1
      else
        freq[number] = 1
      end
    }
    freq.each_pair {|key,value|
      if freq_result[key]
        if freq_result[key] < value
          freq_result[key] = value
        end
      else
        freq_result[key] = value
      end
    }
  }
  
  return freq_result
end
main
