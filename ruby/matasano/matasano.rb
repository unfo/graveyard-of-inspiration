
require './jwcrypto'

module Matasano
  class Set1
    class << self
      def run
        # puts JW.base64encode(JW.hex2bytes('49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d'))
        # task1
        # task2
        # task3
        # task4
        # task5
        task6
      end


      def task1
        #Convert hex to base64
        #The string
        #49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d
        #Should produce
        #SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t

        #So go ahead and make that happen. You'll need to use this code for the rest of the exercises.
        #Cryptopals Rule
        #Always operate on raw bytes, never on encoded strings. Only use hex and base64 for pretty-printing.

        payload = '49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d'
        expected = 'SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t'
        unhexed = JW.hex2bytes(payload)
        b64 = JW.base64encode(unhexed)
        puts "%s = %s\n%s = %s" % [b64, payload, expected, b64 == expected]
      end

      def task2
        #Fixed XOR
        #Write a function that takes two equal-length buffers and produces their XOR combination.
        #If your function works properly, then when you feed it the string
        #1c0111001f010100061a024b53535009181c
        #... after hex decoding, and when XOR'd against
        #686974207468652062756c6c277320657965
        #... should produce
        #746865206b696420646f6e277420706c6179

        hex_payload = '1c0111001f010100061a024b53535009181c'
        xor_key = '686974207468652062756c6c277320657965'
        expected = '746865206b696420646f6e277420706c6179'
        result = JW.bytes2hex(JW.hex_xor(hex_payload, xor_key))

        puts "%s = %s ^ %s\n%s = expected" % [result, hex_payload, xor_key, expected]
      end

      def task3
        #Single-byte XOR cipher
        #The hex encoded string

        #1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736
        #... has been XOR'd against a single character. Find the key, decrypt the message.

        #You can do this by hand. But don't write code to do it for you.

        #How? Devise some method for "scoring" a piece of English plaintext. Character frequency is a good metric.
        #Evaluate each output and choose the one with the best score.

        #Achievement Unlocked
        #You now have our permission to make "ETAOIN SHRDLU" jokes on Twitter.
        payload = '1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736'
        scores = JW.score_single_byte_xors(payload)
        puts scores.first[1]
      end

      def task4
        #Detect single-character XOR
        #One of the 60-character strings in this file (4.txt) has been encrypted by single-character XOR.
        #
        #Find it.
        #
        #(Your code from #3 should help.)
        contenders = IO.readlines('4.txt').collect {|line| [line, JW.score_single_byte_xors(line.chomp).first] }
        winner = contenders.reject {|c| c[1][0] < 0 }.sort_by {|pair| pair[1][0] }.reverse.first
        puts '%s -> %s' % [winner[0].chomp, winner[1][1]]
      end

      def task5
        #Implement repeating-key XOR
        #Here is the opening stanza of an important work of the English language:

        #Burning 'em, if you ain't quick and nimble
        #I go crazy when I hear a cymbal
        #Encrypt it, under the key "ICE", using repeating-key XOR.

        #In repeating-key XOR, you'll sequentially apply each byte of the key; the first byte
        # of plaintext will be XOR'd against I, the next C, the next E, then I again for the
        # 4th byte, and so on.

        #It should come out to:

        #0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272
        #a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f
        #Encrypt a bunch of stuff using your repeating-key XOR function. Encrypt your mail.
        #Encrypt your password file. Your .sig file. Get a feel for it.
        # I promise, we aren't wasting your time with this.
        key = %w{I C E}
        stanza1 = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal"

        enc1 = JW.bytes2hex(JW.xor(stanza1.split(''), key))

        expected1 = '0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f'

        puts "%s\n%s\n%s" % [enc1, expected1, enc1 == expected1]
      end

      def task6
        # This challenge isn't conceptually hard, but it involves actual error-prone coding.
        # The other challenges in this set are there to bring you up to speed.
        # This one is there to qualify you.
        # If you can do this one, you're probably just fine up to Set 6.


        # 6.txt
        # It's been base64'd after being encrypted with repeating-key XOR.
        #
        #  Decrypt it.
        #  Here's how:
        #
        # Let KEYSIZE be the guessed length of the key; try values from 2 to (say) 40.
        # Write a function to compute the edit distance/Hamming distance between two strings.
        # The Hamming distance is just the number of differing bits. The distance between:
        #
        # this is a test
        # and
        # wokka wokka!!!
        #
        # is 37.
        #
        # Make sure your code agrees before you proceed.
        #
        # For each KEYSIZE, take the first KEYSIZE worth of bytes, and the second KEYSIZE worth
        # of bytes, and find the edit distance between them. Normalize this result by dividing by KEYSIZE.
        # The KEYSIZE with the smallest normalized edit distance is probably the key.
        # You could proceed perhaps with the smallest 2-3 KEYSIZE values. Or take 4 KEYSIZE blocks
        # instead of 2 and average the distances.
        # Now that you probably know the KEYSIZE: break the ciphertext into blocks of KEYSIZE length.
        # Now transpose the blocks: make a block that is the first byte of every block,
        # and a block that is the second byte of every block, and so on.
        # Solve each block as if it was single-character XOR. You already have code to do this.
        # For each block, the single-byte XOR key that produces the best looking histogram
        # is the repeating-key XOR key byte for that block. Put them together and you have the key.
        # This code is going to turn out to be surprisingly useful later on.
        # Breaking repeating-key XOR ("Vigenere") statistically is obviously an academic exercise,
        # a "Crypto 101" thing. But more people "know how" to break it than can actually break it,
        # and a similar technique breaks something much more important.
        #
        # No, that's not a mistake.
        # We get more tech support questions for this challenge than any of the other ones.
        # We promise, there aren't any blatant errors in this text.
        # In particular: the "wokka wokka!!!" edit distance really is 37.

        sanity1 = 'this is a test'
        sanity2 = 'wokka wokka!!!'

        puts 'Hamming distance between [%s] and [%s] = %d (expected 37)' % [sanity1, sanity2, JW.hamming(sanity1, sanity2)]
        f = File.open('6.txt', 'r')
        content = JW.base64decode(f.read)
        f.close
        normalized_edit_distances = (2..100).collect do |keysize|
          b1 = content[(0*keysize)...(1*keysize)]
          b2 = content[(1*keysize)...(2*keysize)]
          b3 = content[(2*keysize)...(3*keysize)]
          b4 = content[(3*keysize)...(4*keysize)]
          h1 = JW.hamming(b1,b2)
          h2 = JW.hamming(b1,b3)
          h3 = JW.hamming(b1,b4)
          h4 = JW.hamming(b2,b3)
          h5 = JW.hamming(b2,b4)
          h6 = JW.hamming(b3,b4)
          # avg_hamming = (h1 + h2 + h3 + h4 + h5 + h6) / 6
          [keysize, (h1 / keysize)]
        end

        puts normalized_edit_distances.sort_by {|a| a[1]}.inspect

        top_two = normalized_edit_distances.sort_by {|a| a[1]}.take(5)

        top_two.each do |keysize, _|
          puts 'Trying keysize: %d' % keysize
          re = '.{%s}' % keysize
          blocks = content.split(/#{re}/)
          transposed = (0...keysize).collect do |index|
            blocks.collect {|block| block.size > index ? block[index] : '' }.reject {|b| b == '' }
          end.take(1)

          begin
            # puts transposed.join
            xorred = []
            keys = transposed.collect do |transblock|
              potkeys = JW.score_single_byte_xor(transblock, false)
              unless potkeys.nil? or potkeys.first.nil?
                # puts potkeys.take(3).inspect
                key = potkeys.first[1]
                xorred << JW.xor(transblock, key).join()
                xorred << ' || '
                # puts 'key == [%s]' % key
                key
              end
            end
            puts 'XORRED = %s' % xorred.join
            # puts 'keysize %d keys = %s' % [keysize, keys.inspect]
            puts
          rescue => e
            puts 'problem!'
            puts e
            puts e.backtrace
          end
        end




      end
    end # end << self
  end # end Set1
end # end Matasano


#Matasano::Set1.run

