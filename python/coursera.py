__author__ = 'jw'


class English:
    LETTER_FREQS = {'a': 0.08167, 'b': 0.01492, 'c': 0.02782, 'd': 0.04253, 'e': 0.12702, 'f': 0.02228, 'g': 0.02015,
                    'h': 0.06094, 'i': 0.06966, 'j': 0.00153, 'k': 0.00772, 'l': 0.04025, 'm': 0.02406, 'n': 0.06749,
                    'o': 0.07507, 'p': 0.01929, 'q': 0.00095, 'r': 0.05987, 's': 0.06327, 't': 0.09056, 'u': 0.02758,
                    'v': 0.00978, 'w': 0.02361, 'x': 0.00150, 'y': 0.01974, 'z': 0.00074}


def vignere(_bytes, _key):
    return [chr(b ^ _key[_i % len(_key)]) for _i, b in enumerate(_bytes)]


def is_lowercase_alpha(_byte):
    return ord('a') <= _byte <= ord('z')


def hex_string_to_bytes(hexstr):
    return [int(hexstr[i] + hexstr[i + 1], 16) for i in range(0, len(hexstr) - 1, 2)]


class Assignment1:
    def decipher_assingment(self):
        cipher = 'F96DE8C227A259C87EE1DA2AED57C93FE5DA36ED4EC87EF2C63AAE5B9A7EFFD673BE4ACF7BE8923CAB1ECE7AF2DA3DA44FCF7AE29235A24C963FF0DF3CA3599A70E5DA36BF1ECE77F8DC34BE129A6CF4D126BF5B9A7CFEDF3EB850D37CF0C63AA2509A76FF9227A55B9A6FE3D720A850D97AB1DD35ED5FCE6BF0D138A84CC931B1F121B44ECE70F6C032BD56C33FF9D320ED5CDF7AFF9226BE5BDE3FF7DD21ED56CF71F5C036A94D963FF8D473A351CE3FE5DA3CB84DDB71F5C17FED51DC3FE8D732BF4D963FF3C727ED4AC87EF5DB27A451D47EFD9230BF47CA6BFEC12ABE4ADF72E29224A84CDF3FF5D720A459D47AF59232A35A9A7AE7D33FB85FCE7AF5923AA31EDB3FF7D33ABF52C33FF0D673A551D93FFCD33DA35BC831B1F43CBF1EDF67F0DF23A15B963FE5DA36ED68D378F4DC36BF5B9A7AFFD121B44ECE76FEDC73BE5DD27AFCD773BA5FC93FE5DA3CB859D26BB1C63CED5CDF3FE2D730B84CDF3FF7DD21ED5ADF7CF0D636BE1EDB79E5D721ED57CE3FE6D320ED57D469F4DC27A85A963FF3C727ED49DF3FFFDD24ED55D470E69E73AC50DE3FE5DA3ABE1EDF67F4C030A44DDF3FF5D73EA250C96BE3D327A84D963FE5DA32B91ED36BB1D132A31ED87AB1D021A255DF71B1C436BF479A7AF0C13AA14794'
        cipher_bytes = [int(cipher[i] + cipher[i + 1], 16) for i in range(0, len(cipher) - 1, 2)]
        lengths = range(1, 13)

        len_freqs = dict()
        uniform_p2 = 1.0 / 256.0

        best_candidate = (0, 0.0)

        for length in lengths:
            len_freqs[length] = dict()
            byte_count = 0
            for index in range(0, len(cipher_bytes) - 1, length):
                byte = cipher_bytes[index]
                byte_count += 1
                if byte in len_freqs[length]:
                    len_freqs[length][byte] += 1
                else:
                    len_freqs[length][byte] = 1

            if byte_count > 0:
                byte_count = float(byte_count)

                sum_powers = 0.0
                for byte in len_freqs[length]:
                    probability = len_freqs[length][byte] / byte_count
                    p2 = probability ** 2.0
                    sum_powers += p2

                if sum_powers > uniform_p2 and sum_powers > best_candidate[1]:
                    best_candidate = (length, sum_powers)

        key_length = best_candidate[0]

        secret_key = [i for i in range(0, key_length)]

        for index in range(0, key_length, 1):
            char_freqs = dict()
            sum_probs = dict()
            key_candidate = (0, 0.0)
            for key in range(0, 255):
                char_freqs[key] = dict()
                non_printable_found = False
                lower_case_count = 0.0
                for byte_index in range(index, len(cipher_bytes) - 1, key_length):
                    byte = cipher_bytes[byte_index]
                    xbyte = byte ^ key
                    if xbyte < 32 or xbyte > 127:
                        # print "index(%d) key(%d) ^ byte(%d) = %d" % (index, key, byte, xbyte)
                        non_printable_found = True
                        break

                    if is_lowercase_alpha(xbyte):
                        lower_case_count += 1.0

                    if xbyte in char_freqs[key]:
                        char_freqs[key][xbyte] += 1.0
                    else:
                        char_freqs[key][xbyte] = 1.0

                if non_printable_found:
                    # print "Found non-printables. Not this."
                    del char_freqs[key]
                else:
                    sum_prob = 0.0
                    for byte in char_freqs[key]:
                        if is_lowercase_alpha(byte):
                            english = English.LETTER_FREQS[chr(byte)]
                            prob = char_freqs[key][byte] / lower_case_count
                            sum_prob += english * prob

                    sum_probs[key] = sum_prob

                    print "index(%d) sum_probs[%d] = %f; key_candidate = %d, %f" % (
                        index, key, sum_prob, key_candidate[0], key_candidate[1])

                    if sum_prob > key_candidate[1]:
                        key_candidate = (key, sum_prob)

            secret_key[index] = key_candidate[0]

        print "I think the key = %s" % secret_key
        print "Decrypted: %s" % vignere(cipher_bytes, secret_key)


def other_is_space(a, b):
    return ((a ^ b) & 64 == 64)


class Assignment2:
    """
    Below are 7 ciphertexts, each of which was generated by encrypting some 31-character ASCII plaintext with the
    one-time pad using the same key (code for the encryption program used is given below). Decrypt them and recover all
    7 plaintexts, each of which is a grammatically correct English sentence. Note: for this problem it is easiest to use
    a combination of automated analysis plus human insight and even occasional guessing. As long as you can decrypt them
    all, it doesn't matter how you do it.

    """
    ciphers = [
        'BB3A65F6F0034FA957F6A767699CE7FABA855AFB4F2B520AEAD612944A801E',
        'BA7F24F2A35357A05CB8A16762C5A6AAAC924AE6447F0608A3D11388569A1E',
        'A67261BBB30651BA5CF6BA297ED0E7B4E9894AA95E300247F0C0028F409A1E',
        'A57261F5F0004BA74CF4AA2979D9A6B7AC854DA95E305203EC8515954C9D0F',
        'BB3A70F3B91D48E84DF0AB702ECFEEB5BC8C5DA94C301E0BECD241954C831E',
        'A6726DE8F01A50E849EDBC6C7C9CF2B2A88E19FD423E0647ECCB04DD4C9D1E',
        'BC7570BBBF1D46E85AF9AA6C7A9CEFA9E9825CFD5E3A0047F7CD009305A71E'
    ]

    converted = [hex_string_to_bytes(str) for str in ciphers]
    freqs = English.LETTER_FREQS

    def __init__(self):
        self.hax()

    def hax(self):
        possible_keys = dict()
        same_original_message = dict()
        for outer_index, current_cipher in enumerate(self.converted):
            for index, byte in enumerate(current_cipher):
                for c_index, cipher in enumerate(self.converted):
                    if c_index == outer_index:
                        # No need to self xor
                        continue

                    nth_byte = cipher[index]
                    if other_is_space(byte, nth_byte):
                        # print "%d ^ %d = %d & 64 = %d; %d is space" % (byte, nth_byte, (byte ^ nth_byte), (byte ^ nth_byte) & 64, index)
                        a = byte ^ 0x20
                        b = nth_byte ^ 0x20
                        if index in possible_keys:
                            possible_keys[index].add(a)
                            possible_keys[index].add(b)
                            possible_keys[index].add(0x20)
                        else:
                            possible_keys[index] = set()
                            possible_keys[index].add(a)
                            possible_keys[index].add(b)
                            possible_keys[index].add(0x20)
                    # else:
                    #     print "i%d:[%d]" % (index, (byte ^ nth_byte)),
                        # print "b:%s" % type(byte)
                        # print "nth:%s" % type(nth_byte)
                        # print "----"

                    if (byte ^ nth_byte) == 0:
                        # print "%d is equivalent in both" % index
                        if index in same_original_message:
                            same_original_message[index].append((outer_index, c_index))
                        else:
                            same_original_message[index] = []
                            same_original_message[index].append((outer_index, c_index))

        print ""
        cipher_length = len(self.converted[0])
        # print "The following ciphers share bytes in this position:"
        # for i in range(0, cipher_length):
        #     if i in same_original_message:
        #         print "%2d : %s" % (i, ", ".join([str(x) for x in same_original_message[i]]))
        #
        print "Possible key values:"
        for i in range(0, cipher_length):
            if i in possible_keys:
                print "(%2d): %s" % (i, ", ".join([str(x) for x in possible_keys[i]]))
        #
        # for i1 in possible_keys[1]:
        #     for i2 in possible_keys[2]:
        #         for i3 in possible_keys[3]:
        #             for i4 in possible_keys[4]:
        #                 for i5 in possible_keys[5]:
        #                     key = [242, 26, 4, 155, 208, 115, 0, 0, 0, 0,
        #                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        #                            0, 0, 114, 0, 0, 0, 97, 0, 37, 0, 48]
        #                     chrs = [x if 32 <= ord(x) <= 127 else str(ord(x)) for x in vignere(self.converted[0][0:7], key)]
        #                     chrs2 = [x if 32 <= ord(x) <= 127 else str(ord(x)) for x in vignere(self.converted[2][0:7], key)]
        #                     chrs3 = [x if 32 <= ord(x) <= 127 else str(ord(x)) for x in vignere(self.converted[3][0:7], key)]
        #                     chrs4 = [x if 32 <= ord(x) <= 127 else str(ord(x)) for x in vignere(self.converted[4][0:7], key)]
        #                     print "%s : %s | %s | %s | %s" % (", ".join([str(k) for k in key[0:7]]), "".join(chrs), "".join(chrs2), "".join(chrs3), "".join(chrs4))

        key = [242, 26, 4, 155, 208, 115, 35, 200, 57, 152,
               206, 9, 14, 188, 134, 218, 201, 224, 57, 137,
               42, 95, 114, 103, 131, 165, 97, 253, 37, 238, 48]

        # for kx in range(0,255):
        #     key = [0, 0, 0, 0, 0, 115, 0, 0, 0, 0,
        #            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        #            0, 0, 114, 0, 0, 0, 97, kx, 37, 0, 48]
        #     ok_ciphers = 0
        #     for cipher in self.converted:
        #         chrs = [x if 32 <= ord(x) <= 127 else '_' for x in vignere(cipher, key)]
        #         if 'a' <= chrs[-4] <= 'z' or chrs[-4] == ' ':
        #             ok_ciphers += 1
        #
        #     if ok_ciphers >= len(self.converted) - 1:
        #         print "%d => %d/%d ok ciphers" % (kx, ok_ciphers, len(self.converted))
        for cipher in self.converted:
            chrs = [x if 32 <= ord(x) <= 127 else "["+str(ord(x))+"]" for x in vignere(cipher, key)]
            print "".join(chrs)
        #     else:
        #         print "%d => %d/%d ok ciphers" % (kx, ok_ciphers, len(self.converted))

class Assignment3:
    """

    """
Assignment2()
