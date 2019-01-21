
class Primer
  require 'log4r'
  include Log4r

  @log = Logger.new 'log'
  so = StdoutOutputter.new 'console'
  so.only_at INFO
  @log.add(so)

  def self.get_primes(number, limit, known_primes)
    while known_primes.last < limit
      @log.debug 'Processing %d' % number
      divisable = false
      known_primes.each {|prime|
        if prime < Math.sqrt(number).to_i
          if number % prime == 0
            divisable = true
            @log.debug '%d %% %d == 0' % [number, prime]
          end
        end
      }
      if not divisable
        known_primes << number
        @log.info known_primes.size.to_s + ". " + number.to_s
      end
      number +=2
    end
    return known_primes

  end
end
