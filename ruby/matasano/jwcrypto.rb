require 'base64'

module Matasano
  class JW

    class << self
      def base64encode(bytes)
        Base64.strict_encode64(bytes.join)
      end
      def base64decode(str)
        Base64.decode64(str)
      end

      def hex2bytes(hex)
        hex.scan(/../).map {|hx| [hx].pack('H*') }
      end

      def bytes2hex(bytes)
        bytes.join.unpack('H*').join
      end

      def hex_xor(hxa, hxb)
        bytesa = hex2bytes(hxa)
        bytesb = hex2bytes(hxb)

        xor(bytesa, bytesb)
      end

      def xor(a, b)
        a.each_with_index.map do |byte, index|
          (byte.ord ^ b[index % b.length].ord).chr
        end
      end

      def score_single_byte_xors(payload, hexed=true)

        content = payload
        content = hex2bytes(payload) if hexed

        cleartexts = (32..128).map {|key| xor(content, [key]).join }
        contenders = cleartexts.collect do |clr|
          nonprint = clr.split('').inject(0) do |accu, elem|
            (elem.ord < 0x20 || elem.ord > 0x7E ? accu + 1 : accu )
          end
          clr
        end
        contenders.reject! {|c| c.nil? }

        contenders.collect {|c| [english_score(c), c]}.sort_by{|pair| pair[0]}.reverse
      end

      def score_single_byte_xor(payload, hexed=true)

        content = payload
        content = hex2bytes(payload) if hexed

        cleartexts = (1..255).map {|key| [key.chr, xor(content, [key]).join] }
        contenders = cleartexts.collect do |clr|
          nonprint = clr[1].split('').inject(0) do |accu, elem|
            (elem.ord < 0x20 || elem.ord > 0x7E ? accu + 1 : accu )
          end
          clr if nonprint < 5
        end
        contenders.reject! {|c| c.nil? }

        contenders.collect {|c| [english_score(c[1]), c[0]]}.sort_by{|pair| pair[0]}.reverse
      end

      def find_xor_key_length(txt)
        min_samples = txt.size / 8
        (1..8).collect do |keylen|
          sample = (0..min_samples).step(keylen).collect {|i| txt[i*keylen] }
          [keylen, english_score(sample.join(''))]
        end
      end

      def english_score(txt)
        most_frequent = 'ETAON RISHDLFCMUGYPWBVKJXQZ'.split('')

        letter_scores = Hash.new(-10)
        most_frequent.each_with_index do |letter, index|
          letter_scores[letter] = most_frequent.length * 3 - (index * 2)
        end

        txt.split('').inject(0) {|score, letter| score + letter_scores[letter.upcase]}
      end

      def hamming(a, b)
        xor(a.split(''),b.split('')).inject(0) {|ones, x| ones + x.ord.to_s(2).scan(/1/).size }
      end

      def ascii_weight(chr)
        freq = {'h' => 3.834, '8' => -3.665, '/' => -2.951, '9' => -3.708, '"' => 1.68, 'y' => 2.841, 'w' => 2.919, '&' => -6.843, 'Z' => -5.745, 'a' => 4.086, ':' => -0.512, 'I' => 1.616, 'p' => 2.403, '%' => -6.15, 'c' => 2.82, 'u' => 3.13, 'S' => 0.296, '2' => -3.13, '.' => 2.208, '4' => -3.393, 'X' => -2.007, 'G' => -0.69, 'e' => 4.522, 'R' => -0.734, '6' => -3.708, 't' => 4.231, 'v' => 1.869, 'r' => 3.709, 'x' => -0.064, '\n' => 3.028, 's' => 3.79, ';' => 0.742, '[' => -4.401, 'L' => -0.298, 'N' => -0.474, 'i' => 3.838, 'Q' => -2.892, '0' => -2.972, '3' => -3.26, '@' => -5.457, 'm' => 2.874, 'U' => -1.687, 'o' => 4.088, '1' => -1.942, "'" => 1.816, '_' => 0.436, '#' => -6.15, 'T' => 0.997, 'g' => 2.788, '(' => -2.253, ')' => -2.253, 'A' => 0.378, ',' => 2.595, 'b' => 2.442, '?' => -0.009, 'J' => -0.472, '7' => -3.823, 'B' => 0.236, 'D' => -0.312, 'H' => 0.415, 'n' => 3.979, 'f' => 2.724, ' ' => 5.13, 'E' => -0.024, '-' => 1.42, 'M' => 0.32, 'j' => 0.078, 'd' => 3.568, 'V' => -2.248, 'W' => 0.331, 'q' => -0.497, 'Y' => -0.593, 'k' => 1.963, '$' => -5.339, ']' => -4.317, 'C' => -0.308, 'l' => 3.402, '*' => -2.208, 'O' => -0.6, 'F' => -1.026, '!' => 0.123, 'P' => -0.355, 'K' => -2.015, 'z' => -0.32, '5' => -3.288}
        freq[chr].nil? ? -100 : freq[chr]
      end

    end
  end
end